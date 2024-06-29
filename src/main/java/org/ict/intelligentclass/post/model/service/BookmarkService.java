package org.ict.intelligentclass.post.model.service;

import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.post.jpa.entity.BookmarkEntity;
import org.ict.intelligentclass.post.jpa.repository.BookmarkRepository;
import org.ict.intelligentclass.post.jpa.repository.PostRepository;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public BookmarkEntity addBookmark(Long postId, String userEmail, String provider) {
        BookmarkEntity bookmark = new BookmarkEntity();
        bookmark.setPostId(postId);
        bookmark.setUserEmail(userEmail);
        bookmark.setProvider(provider);
        bookmark.setBookmarkDate(LocalDateTime.now());

        return bookmarkRepository.save(bookmark);
    }

    public void removeBookmark(Long postId, String userEmail, String provider) {
        Optional<BookmarkEntity> bookmark = bookmarkRepository.findByPostIdAndUserEmailAndProvider(postId, userEmail, provider);
        bookmark.ifPresent(bookmarkRepository::delete);
    }

    public List<BookmarkEntity> getUserBookmarks(String userEmail, String provider) {
        return bookmarkRepository.findByUserEmailAndProvider(userEmail, provider);
    }

    public boolean isPostBookmarked(Long postId, String userEmail, String provider) {
        return bookmarkRepository.findByPostIdAndUserEmailAndProvider(postId, userEmail, provider).isPresent();
    }
}
