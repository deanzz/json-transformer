package dean.tools.json.transform.dao.mongo

import dean.tools.json.transform.dao.DaoComponent
import org.mongodb.scala.MongoCollection

trait MongoDaoComponent extends DaoComponent{
  trait MongoAbstractDao[R, U] extends AbstractDao[R, U]{
    val collectionName: String
    val collection: MongoCollection[_]
  }
}
