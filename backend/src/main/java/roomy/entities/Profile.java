package roomy.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "profile")
public class Profile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String fullName;
    private Integer age;
    private String gender;
    private String occupation;
    private String bio;
    private String city;
    private String profilePicUrl;

    private String lifestyle;
    private String socialLinks;
}
