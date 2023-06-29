package com.neymeha.socialmediasecurityapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


// наш энтити класс для связи обьекта пользователя с БД который имплиментирует UserDetails для дальнейших действий по реализации безопасности
@Getter
@Setter
@Builder
@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    private String name;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private List<Long> userIdRequestedForFriendship;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "main_user_id")
    private Map<User, Status> statusWithUsers = new HashMap<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Map<User, MessageHistory> messageHistories = new HashMap<>();

    public void recieveFriendRequest(long userId){
        if(userIdRequestedForFriendship ==null){
            userIdRequestedForFriendship = new ArrayList<>();
        }
        userIdRequestedForFriendship.add(userId);
    }

    public void replyFriendRequest(long userId){
        userIdRequestedForFriendship.remove(userId);
    }

    public void addToFriendList(User user, Status status){
        statusWithUsers.put(user, status);
    }

    public void deleteFriend(User user, Status status) { statusWithUsers.replace(user, status); }

    public void createPost(Post post){
        posts.add(post);
    }

    public void deletePost(Post post){
        posts.remove(post);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    } // явно перезаписываем метод так как использовали аннотации для геттеров и сеттров а реализация нужна другая

    @Override
    public String getPassword() {
        return password;
    } // явно перезаписываем метод для наглядности из за аннотаций

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } // у аккаунтов нет срока действия

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } // не брокируем аккаунты в проекте

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } // не используем credentials в проекте

    @Override
    public boolean isEnabled() {
        return true;
    } // не используем активацию аккаунта в приложении

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getUserId() == user.getUserId() && Objects.equals(getEmail(), user.getEmail()) && getRole() == user.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmail(), getRole());
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", userIdRequestedForFrandship=" + userIdRequestedForFriendship +
                ", userPosts=" + posts +
                ", statusWithUsers=" + statusWithUsers +
                '}';
    }
}
