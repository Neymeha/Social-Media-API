package com.neymeha.socialmediasecurityapi.service.feed;

import com.neymeha.socialmediasecurityapi.controller.feed.FeedRequest;
import com.neymeha.socialmediasecurityapi.controller.feed.FeedResponse;
import com.neymeha.socialmediasecurityapi.customexceptions.post.NoPostsUnderSubscriptionsException;
import com.neymeha.socialmediasecurityapi.customexceptions.request.PageOutOfBoundsException;
import com.neymeha.socialmediasecurityapi.customexceptions.request.RequestException;
import com.neymeha.socialmediasecurityapi.customexceptions.status.NoSubscriptionException;
import com.neymeha.socialmediasecurityapi.customexceptions.user.UserNotFoundException;
import com.neymeha.socialmediasecurityapi.entity.Status;
import com.neymeha.socialmediasecurityapi.entity.User;
import com.neymeha.socialmediasecurityapi.entity.Post;
import com.neymeha.socialmediasecurityapi.repository.UserRepository;
import com.neymeha.socialmediasecurityapi.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public FeedResponse showFeed(FeedRequest request){

        if(request.getPageNo()<1){ // проверки на адекватность данных в запросе
            throw new RequestException("Page number could not be less than 1", HttpStatus.BAD_REQUEST);
        } else if (request.getPageSize()<1){
            throw new RequestException("Page size could not be less than 1", HttpStatus.BAD_REQUEST);
        }

        var user = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        List<User> subscriberList; // локальный список из пользователей на которых подписан главный пользователь
        List<Post> paginatedPosts; // локальный список постов по подпискам с использованием пагинации постов
        int totalPg; // локальная переменная для хранения общего колличество страниц с учетом запрашиваемого колличества постов на этих страницах
        int lastPosts=0; // локальная переменная для хранения колличества излишка постов если таковые есть на последней странице
        boolean lastPg=false; // локальная переменная для понимания есть ли страница с излишком постов
        Status sts = Status.builder().subscription(true).build(); // локальная переменная для фильстрации пользователей по наличию подписки на них
        Map <User,Status> statusMap = user.getStatusWithUsers(); // получаем мапу Пользователь/Статус с пользователем

        Map <User,Status> filteredBySubsStatusMap = statusMap.entrySet()
                .stream()
                .filter(x->sts.mySubOrNot(x.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); //фильтруем мапу пользователь/статус по наличию подписки на данных пользователей у главного пользователя
        subscriberList = filteredBySubsStatusMap.keySet().stream().toList(); // присваиваем локальному списку всех пользователей с положительным статусом подписок
        if (subscriberList.isEmpty()){ // если подписок нет - дальше идти нет смысла - выбрасываем исключение
            throw new NoSubscriptionException("There no subscription to any user!", HttpStatus.NOT_FOUND);
        }

        List <Post> postList = subscriberList // получаем все посты у всех пользователей с положительной подпиской
                .stream()
                .flatMap(u->u.getPosts().stream())
                .sorted(Comparator.comparing(Post::getTimestamp)) // и сортируем их по времени создания
                .collect(Collectors.toList());
        if (postList.isEmpty()){ // если пользватели по подпискам не создавали постов - дальше идти нет смысла - бросаем исключение
            throw new NoPostsUnderSubscriptionsException("There are no posts under your subscriptions!", HttpStatus.NOT_FOUND);
        }

        totalPg = postList.size() / request.getPageSize(); // присваиваем переменной колличество страниц для пагинации

        if (postList.size()%request.getPageSize()!=0){ // если есть излишек постов
            lastPosts=postList.size()%request.getPageSize(); // присваиваем переменной колличество этого излишка
            totalPg++; // добавляем страницу в общее колличество так как есть излишек
            lastPg=true; // булевое значение меняем так как есть последняя страница
        }

        if (request.getPageNo()>totalPg){ // запрашиваемая страница не может превышать рамки общего колличества страниц удовлитворяющих условию запроса
            throw new PageOutOfBoundsException("Requested page is out of bounds of total pages available", HttpStatus.BAD_REQUEST);
        }

        if (request.getPageNo()==1){ // настраиваем пагинацию страниц, если 1 страница
            paginatedPosts = postList.subList(request.getPageNo()-1, (request.getPageNo()) * request.getPageSize());
        } else if (lastPg){ // если последняя страница
            paginatedPosts = postList.subList( ((request.getPageNo()-1)* request.getPageSize()), ((request.getPageNo()) * request.getPageSize()) - (request.getPageSize()-lastPosts));
        } else { // если любая другая страница
            paginatedPosts = postList.subList( ((request.getPageNo()-1)* request.getPageSize()), (request.getPageNo()) * request.getPageSize());
        }

        return FeedResponse.builder() // создаем обьект ответа для возврата его пользователю
                .posts(paginatedPosts) // список пагинируемых постов
                .pageNo(request.getPageNo()) // номер текущей страницы по запросу
                .pageSize(request.getPageSize()) // обьем запрашиваемой страницы
                .tatalPages(totalPg) // общее колличество страниц с текущими условиями
                .build();
    }
}
