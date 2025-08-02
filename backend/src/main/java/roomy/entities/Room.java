package roomy.entities;


import jakarta.persistence.Entity;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import roomy.entities.enums.RoomType;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String title;
    private String description;
    private String location;
    private String city;
    private Integer rent;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private LocalDate availableFrom;

    private String images; // List of URLs

    private LocalDateTime createdAt = LocalDateTime.now();
}