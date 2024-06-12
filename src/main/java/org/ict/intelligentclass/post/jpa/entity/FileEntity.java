package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_FILE")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_FILE_ID")
    @SequenceGenerator(name = "SQ_FILE_ID", sequenceName = "SQ_FILE_ID", allocationSize = 1)
    @Column(name = "POST_FILE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private PostEntity post;

    @Column(name = "POST_FILE_URL", nullable = false)
    private String fileUrl;

    @Column(name = "FILE_UPLOAD_TIME", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime uploadTime;
}
