package at.vs.vsmarkthartmannsdorf.data;

import at.vs.vsmarkthartmannsdorf.serializer.LocalDateDeserializer;
import at.vs.vsmarkthartmannsdorf.serializer.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.AccessibleObject;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherAbsence {
    private int teacherID;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fromDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate toDate;
    private String reason;

    @JsonIgnore
    private static DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @JsonIgnore
    @Override
    public String toString() {
        if (reason.isBlank()) {
            return "Abwesend von: " + fromDate.format(dTF) + " bis: " + toDate.format(dTF);
        }
        return "Abwesend von: " + fromDate.format(dTF) + " bis: " + toDate.format(dTF) + " mit dem Grund: " + reason;
    }
}
