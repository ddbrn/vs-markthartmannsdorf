package at.vs.vsmarkthartmannsdorf.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subjectobject {
    private String name;
    private double colorred;
    private double colorgreen;
    private double colorblue;
    @JsonIgnore
    public Color getColor(){
        return Color.color(colorred, colorgreen,colorblue);
    }
}
