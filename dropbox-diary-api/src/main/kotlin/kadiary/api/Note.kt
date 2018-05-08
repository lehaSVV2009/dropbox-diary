package kadiary.api

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.format.annotation.DateTimeFormat

import javax.validation.constraints.NotNull

/**
 * Diary entry
 */
class Note {

    /**
     * Unique auto-generated id
     */
    @Id
    var id: String? = null

    /**
     * Any text
     */
    @NotNull
    var text: String? = null

    /**
     * Created date
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    val date: java.util.Date? = null
}
