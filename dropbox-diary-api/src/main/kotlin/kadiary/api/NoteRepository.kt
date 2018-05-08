package kadiary.api

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * CRUD repository for diary entry
 */
@RepositoryRestResource(collectionResourceRel = "notes", path = "notes")
interface NoteRepository : MongoRepository<Note, String>
