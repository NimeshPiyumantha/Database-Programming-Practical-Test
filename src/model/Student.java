package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private String studentId;
    private String studentName;
    private String email;
    private String contact;
    private String address;
    private String nic;


}
