package kadiary

import com.dropbox.core.DbxException
import spock.lang.Specification
import spock.lang.Unroll

import java.time.ZoneOffset
import java.time.ZonedDateTime

class DiaryServiceSpec extends Specification {

  DiaryService diaryService
  DiaryRepository diaryRepository

  def setup() {
    diaryRepository = Mock(DiaryRepository)
    diaryService = new DiaryService(diaryRepository)
  }

  @Unroll
  def 'should fail when events are invalid #events'() {
    when:
    diaryService.addEvents(events)

    then:
    thrown(DbxException)

    where:
    events                  | _
    null                    | _
    []                      | _
    [new Event(text: null)] | _
  }

  def 'should add new event item to yaml file'() {
    given:
    def text = 'test dropbox integration'
    def date = ZonedDateTime.now(ZoneOffset.UTC)
    def event = new Event(text, date)

    def oldDiary = '---\nevents:\n- text: "test"\n  date: "2018-06-01T22:56:01.46Z"\n'
    diaryRepository.read() >> oldDiary

    when:
    diaryService.addEvents([event])

    then:
    noExceptionThrown()

    interaction {
      String newDiary = "$oldDiary- text: \"$text\"\n  date: \"${date.toString()}\"\n"
      1 * diaryRepository.write(newDiary)
    }
  }
}
