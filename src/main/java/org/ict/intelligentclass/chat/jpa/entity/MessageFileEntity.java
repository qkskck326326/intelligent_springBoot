package org.ict.intelligentclass.chat.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_MESSAGEFILE")
public class MessageFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_FILE_ID")
    @SequenceGenerator(name = "SQ_FILE_ID", sequenceName = "SQ_FILE_ID", allocationSize = 1)
    @Column(name="FILE_ID")// 파일 일련번호
    private Long fileId;

    @Column(name="MESSAGE_ID", nullable = false)//메시지 일련번호
    private Long messageId;

    @Column(name="SENDER_ID", nullable = false)//송신자
    private String senderId;

    @Column(name="FILE_URL", nullable = false)//파일 주소
    private String fileURL;

    @Column(name="FILE_SIZE", nullable = false)//파일 사이즈
    private String fileSize;

    @Column(name="ORIGINAL_NAME", nullable = false)//파일 원본명
    private String originalName;

    @Column(name="RENAMED_NAME", nullable = false)//파일 변경명
    private String renamedName;
}
