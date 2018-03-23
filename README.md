# 如何只依靠Scala的语言特性实现高逼格的依赖注入

>什么是依赖注入？<br/>
对象是面向对象世界中的节本组成单元，依赖注入将对象组合在一起。以最简单的方式看，依赖注入所做的事情就是通过构造器或setter方法将依赖注入到对象。<br/>
如果使用适当的容器，我们还可以将系统每个组件的依赖都抽取到配置文件或配置代码中，并在需要的时候由容器完成注入，这就是高逼格的依赖注入。<br/>
依赖注入的好处其实就是【解耦】，<br/>
如果配合接口、抽象类等使用，还会收获易扩展、易维护的好处。

>Scala如何实现？<br/>
在Scala中可以使用Java中传统的依赖注入框架，比如Guice，但是无一例外，他们都需要一个外部的框架来起作用。<br/>
使用Scala也可以在不使用任何框架的情况下，依靠Cake模式实现高逼格的依赖注入，这就是今天要讲的主要内容。<br/>
下面我们来看一个真实的例子。

## 需求
最近我在做【增加运算模块的列筛选条件支持】需求，其中包含两大部分，
第一个是系统内代码的改造，第二个是为了兼容之前流程图，需要将现存所有相关的24个模块的workflow.param字段值更新为新版本的json。
我们用第二个需求，来讲述如何用Cake模式实现依赖注入。

## 设计
看到这个需求，我们会想到需要做如下事情：
1. 从mongodb读出每个模块的workflow.param字段中的json
2. 将1中旧版json转换成新版json
3. 将2中新版json更新到workflow.param字段

如果单纯只是实现上面的1、2、3，其实可以写一个足够简单的程序包含3个文件即可，<br/>
一个文件中写24个模块的json转换逻辑，一个文件写读写mongodb的逻辑，一个文件写循环调用每个模块json解析和更新的逻辑。<br/>
但是你有没有想过，<br/>
如果以后出了一款新的mongodb驱动，性能超好，想替换现有db读取逻辑呢？<br/>
如果以后mongodb换成了mysql呢？<br/>
如果以后出了一款新的json解析库，性能超好，想替换现有json处理逻辑呢？<br/>
如果以后想部分模块增加一个将json转换成xml的逻辑呢？<br/>
如果以后不仅想用log4j写日志，还想采用自己开发的一个模块将日志输出到hdfs呢？<br/>
...<br/>
其实针对现有需求，想这些未来可扩展的地方的确是多余的，这里纯为了演示高逼格依赖注入的扩展维护能力，<br/>
但是大家在每次设计程序的时候还是要多思考一下，一是锻炼自己设计程序的能力，二是以后真的要扩展了，会相对容易很多，你会很爽。<br/>
大家需要找到易扩展维护和实现难度的平衡。<br/>

所以我的设计是这样的，
1. 抽象出一个json解析层，将json解析看成一个组件，24个模块会存在24个json解析组件
2. 抽象出一个数据访问层，将读写数据库抽象成一个组件，目前只有mongodb一种数据库，则只有mongodb对应的组件
3. 抽象出一个日志输出层，将日志输出抽象成一个组件
4. 抽象出一个job执行任务层，它由各种组件组成，这里可以采用如下设计<br/>
        a.将json组件和db组件安装到一个job，这样就是一个job模块会处理自己的json和更新自己的字段<br/>
        b.只将json组件安装到job，外部获取所有job的新版json结果，批量更新db<br/>
   此次我采用b方案，因为采用a方案，由于数据库连接数过多，数据库会报"too many open files"，然后挂掉。

5. 抽象出一个launch加载层，它由db组件和log组件组成，用来调用24个模块的json转换和批量更新数据库的逻辑

上面描述的前3个组件会使用Cake模式被注入到job层或launch层中。

## 编码
### 接口定义
首先看一下各层次的抽象接口。
###### 数据访问层 DBPluginComponent
```scala
trait DBPluginComponent {
  trait DBPlugin[R, U]{
    def queryByNodeTypes(nodeTypes: Seq[NodeType]): Future[Seq[R]]
    def bulkUpdateParam(workflowWithParamSeq: Seq[(R, String)]): Future[U]
  }
}
```
trait套trait是不是看着很新鲜，的确这种写法不常用，但是这就是Cake模式的一部分：
第一步，先定义将要被注入的依赖。
此处定义了将要被注入的依赖：数据访问组件。

###### json解析层 JsonPluginComponent
```scala
trait JsonPluginComponent {
  trait JsonPlugin[O] {
    def fromOld(jsonStr: String): O = {
      val json = Try(JSON.parseArray(jsonStr).getJSONObject(0)).getOrElse(JSON.parseObject(jsonStr))
      fromOld(json)
    }
    def fromOld(json: JSONObject): O = throw new UnsupportedOperationException
    def toNew(oldObj: O): String
    def transform(json: String): String = {
      toNew(fromOld(json))
    }
    protected def toSelectedColumnsJson(columns: JSONArray): JSONObject = {
      val json = new JSONObject()
      json.put("isSpecified", true)
      json.put("specifiedColumns", columns)
      json
    }
  }
}
```
此处定义了将要被注入的依赖：json解析组件。
fromOld方法是将旧版json转为中间结构，toNew方法将中间结构转为新版json，transform方法被外部调用。

###### 日志输出层 LoggerComponent
```scala
trait LoggerComponent {
  trait Logger {
    def info(s: String)
    def warn(s: String)
    def warn(s: String, e: Throwable)
    def error(s: String)
    def error(s: String, e: Throwable)
    def debug(s: String)
  }
}
```
此处定义了需要注入的依赖：日志输出组件

###### job执行任务层 JobComponent
```scala
trait JobComponent[R <: NodeTypeWithParam] {
  this: JsonPluginComponent =>
  val json: JsonPlugin[_]
  def work(workflow: R): (R, String) = {
    val newJson = json.transform(workflow.param)
    (workflow, newJson)
  }
}
```
此处定义了一个job组件，它告诉程序，必须将json解析组件依赖注入进来，所以这里使用这种写法，
```scala
this: JsonPluginComponent =>
```
这种写法被称作自身类型标注（self-type annotation），它可以确保Cake模式的类型安全，
即混入了JobComponent特质的类，同样也必须混入特质JsonPluginComponent，否则编译报错。
这就是Cake模式的第二步，定义该注入什么类型的依赖。
下面的代码告诉job，这里需要一个json解析组件，但是具体是哪个组件，从哪来，由下游具体实现决定。
```scala
val json: JsonPlugin[_]
```
work方法定义了任务如何依靠注入的组件干活，实现类不用关心这个方法，只需关心具体要注入什么组件。

###### launch加载层
```scala
trait LaunchComponent[R <: NodeTypeWithParam, U] {
  this: DBPluginComponent with LoggerComponent =>
  val db: DBPlugin[R, U]
  val logger: Logger
  def launch(): Unit = {
    val filterNodesConfMap: Map[NodeType, String] = ConfigurationFactory.get.getConfigList("filter.nodes").asScala.map(FilterNode.apply).toMap
    val nodeJobMap = new mutable.HashMap[NodeType, JobComponent[R]]
    val filterNodeTypes = filterNodesConfMap.keys.toSeq
    val filterNodes = Await.result(db.queryByNodeTypes(filterNodeTypes), 60 second)
    logger.info(s"QueriedCount = ${filterNodes.size}")
    val newParamSeq = filterNodes.map {
      n =>
        val job = nodeJobMap.getOrElse(n.nodeType, {
          val instance = this.getClass.getClassLoader.loadClass(filterNodesConfMap(n.nodeType)).newInstance().asInstanceOf[JobComponent[R]]
          nodeJobMap += (n.nodeType -> instance)
          instance
        })
        job.work(n)
    }
    workResultHandler(newParamSeq)
  }
  def workResultHandler(seq: Seq[(R, String)]): Unit
}
```
此处定义了一个launch组件，它告诉程序，必须将数据访问组件和日志输出组件注入进来，同样也使用了自身类型标注的写法，确保类型安全。
下面的代码告诉launch组件，这里需要一个数据访问组件和一个日志输出组件，但是具体是哪个组件，从哪来，由下游具体实现决定。
```scala
  val db: DBPlugin[R, U]
  val logger: Logger
```
launch方法描述如何加载各job组件完成任务，其中会根据配置文件动态实例化需要的job层组件，
workResultHandler方法描述如何处理所有job组件的处理结果，需要下游实现。

### 接口实现
再看一下每个层次是如何实现接口的，由于模块较多，json解析层和job任务执行层只挑选其中一个模块做说明。

###### 数据访问层mongodb组件 MongoDBComponent
```scala
trait MongoDBComponent extends DBPluginComponent {
  class MongoDB extends DBPlugin[MongoWorkFlowParam, BulkWriteResult] {
    private val uri = ConfigurationFactory.get.getString("db.mongo.uri")
    private val dbName = ConfigurationFactory.get.getString("db.mongo.dbName")
    private val db = MongoClient(uri).getDatabase(dbName)
    private val collectionName = "workflow"
    import org.mongodb.scala.bson.codecs.Macros._
    private implicit val codecRegistry: CodecRegistry = fromRegistries(
      DEFAULT_CODEC_REGISTRY,
      fromCodecs(new NodeTypeCodec),
      fromProviders(classOf[MongoWorkFlowParam])
    )
    private val collection = db.getCollection[MongoWorkFlowParam](collectionName).withCodecRegistry(codecRegistry)
    private val documentCollection = db.getCollection[Document](collectionName)
    override def queryByNodeTypes(nodeTypes: Seq[NodeType]): Future[Seq[MongoWorkFlowParam]] = {
      if (nodeTypes.isEmpty) Future(Seq.empty[MongoWorkFlowParam])
      else {
        collection.find(
          and(in("nodeType", nodeTypes: _*),
            notEqual("param", ""),
            notEqual("param", "[]"),
            notEqual("param", null),
            notEqual("param", "{}"))).projection(include("id" ,"nodeType", "param")).toFuture()
      }
    }
    override def bulkUpdateParam(workflowWithParamSeq: Seq[(MongoWorkFlowParam, String)]): Future[BulkWriteResult] = {
      val writes: Seq[WriteModel[_ <: Document]] = workflowWithParamSeq.map{
        case (w, newJson) =>
          UpdateOneModel(Document("_id" -> w._id), set("param", newJson))
      }
      documentCollection.bulkWrite(writes).toFuture()
    }
  }
}
```
下游的组件没什么特别的，就是根据自身的特性，实现接口中的方法，这里是使用了"mongo-scala-driver"驱动实现的访问mongodb的方法。

###### 数据访问层dummy测试组件 DummyDBComponent
```scala
trait DummyDBComponent extends DBPluginComponent {
  class MongoDB extends DBPlugin[MongoWorkFlowParam, BulkWriteResult] {
    private val uri = ConfigurationFactory.get.getString("db.mongo.uri")
    private val dbName = ConfigurationFactory.get.getString("db.mongo.dbName")
    private val db = MongoClient(uri).getDatabase(dbName)
    private val collectionName = "workflow"
    import org.mongodb.scala.bson.codecs.Macros._
    private implicit val codecRegistry: CodecRegistry = fromRegistries(
      DEFAULT_CODEC_REGISTRY,
      fromCodecs(new NodeTypeCodec),
      fromProviders(classOf[MongoWorkFlowParam])
    )
    private val collection = db.getCollection[MongoWorkFlowParam](collectionName).withCodecRegistry(codecRegistry)
    override def queryByNodeTypes(nodeTypes: Seq[NodeType]): Future[Seq[MongoWorkFlowParam]] = {
      if (nodeTypes.isEmpty) Future(Seq.empty[MongoWorkFlowParam])
      else {
        collection.find(and(in("nodeType", nodeTypes: _*), notEqual("param", ""), notEqual("param", "[]"), notEqual("param", null), notEqual("param", "{}"))).projection(include("id" ,"nodeType", "param"))/*.limit(1)*/.toFuture()
      }
    }
    override def bulkUpdateParam(workflowWithParamSeq: Seq[(MongoWorkFlowParam, String)]): Future[BulkWriteResult] = {
      Future(BulkWriteResult.unacknowledged())
    }
  }
}
```
这个实现是为了开发过程中测试用的，
因为在开发完json解析的job部分后，我测试时只想读出我需要的旧版json参数，但不想真正更新数据库。
于是我只需将这个Dummy组件注入到launch层的实现Launcher，其他代码不变，就可达到测试目的。

###### json解析层列加密模块组件 ColumnEncryptJsonComponent
```scala
trait ColumnEncryptJsonComponent extends JsonPluginComponent {
  class ColumnEncryptJson extends JsonPlugin[OldColumnEncryptParam] {
    override def fromOld(json: JSONObject): OldColumnEncryptParam = {
      val retainOldColumn = if (json.containsKey("retainOldColumn")) Some(json.getBooleanValue("retainOldColumn")) else None
      val selectedArr = if (json.containsKey("selected")) Some(json.getJSONArray("selected")) else None
      OldColumnEncryptParam(selectedArr, retainOldColumn)
    }
    override def toNew(oldObj: OldColumnEncryptParam): String = {
      val rootJson = new JSONArray()
      val newJson = new JSONObject()
      oldObj.selected.map {
        v =>
          val selectedColumnsJson = toSelectedColumnsJson(v)
          newJson.put("selectedColumns", selectedColumnsJson)
      }
      oldObj.retainOldColumn.map(v => newJson.put("retainOldColumn", v))
      rootJson.add(newJson)
      rootJson.toJSONString
    }
  }
  case class OldColumnEncryptParam(selected: Option[JSONArray],
                                   retainOldColumn: Option[Boolean])
}
```
这个就是列加密模块json解析转换的具体实现，没啥可说的，看代码。

###### job执行任务层列加密模块组件 ColumnEncryptJob
```scala
class ColumnEncryptJob extends JobComponent[MongoWorkFlowParam]
  with ColumnEncryptJsonComponent {
  override val json  = new ColumnEncryptJson
}
```
这个就是列加密模块job层的实现，其中注入了列加密模块的json解析组件。

###### launch加载层的具体实现组件 Launcher
```scala
class Launcher extends LaunchComponent[MongoWorkFlowParam, BulkWriteResult]
  with MongoDBComponent with Log4jLoggerComponent {
  override val db: DBPlugin[MongoWorkFlowParam, BulkWriteResult] = new MongoDB
  override val logger = new Log4jLogger(this.getClass)
  override def workResultHandler(seq: Seq[(MongoWorkFlowParam, String)]): Unit = {
    val start = System.currentTimeMillis()
    val future = db.bulkUpdateParam(seq)
    val result = Await.result(future, 300 second)
    val finished = System.currentTimeMillis()
    logger.info(s"Elapsed time = ${finished - start}ms, MatchedCount = ${result.getMatchedCount}, ModifiedCount = ${result.getModifiedCount}, DeletedCount = ${result.getDeletedCount}, InsertedCount = ${result.getInsertedCount}")
  }
}
```
这个就是launch层加载器的具体实现了，其中注入了"mongo-scala-driver"驱动实现的数据访问层的组件和log4j实现的日志输出层组件，
workResultHandler方法中处理了将所有job组件生成的新版json批量更新到数据库的逻辑。

### 扩展与维护
上面已经完整的演示了如何只依靠Scala的语言特性实现高逼格的依赖注入。
下面我们来看看咱们实现的依赖注入，是如何快速解决文章开头提出的几个未来可能变化的需求的。

###### 如果以后出了一款新的mongodb驱动，性能超好，想替换现有db读取逻辑呢？
1. 新写一个数据访问层mongodb组件 MongoDBV2Component
2. 将新写的MongoDBV2Component组件注入到加载层具体实现的Launcher
   将原有组件的注入代码
   ```scala
    with MongoDBComponent
   ```
   替换成新组件的注入代码
   ```scala
    with MongoDBV2Component
   ```
3. 修改泛型需要的数据类型（也可能不用修改）

###### 如果以后mongodb换成了mysql呢？
与上面问题的解决方案基本一致，只不过是新写一个针对mysql的数据访问层组件即可

###### 如果以后出了一款新的json解析库，性能超好，想替换现有json处理逻辑呢？
1. 每个模块新写一个使用新解析库实现的json解析层的组件 
   这一步的工作省不了，因为每个模块的json都是定制的。
2. 将1中新写的组件替换原有json解析组件，注入到对应的job实现中
3. 修改泛型需要的数据类型（也可能不用修改）

###### 如果以后想部分模块增加一个将json转换成xml的逻辑呢
1. 新增一个xml解析层和接口，使用trait嵌套的方式，定义将要被注入的xml解析组件依赖
2. 使用一种xml解析库实现1中的接口
3. 在需要json转xml的job中，将xml解析实现组件注入进去，比如
   ```scala
    with ColumnEncryptJsonComponent
   ```
   修改成
   ```scala
    with ColumnEncryptJsonComponent with ColumnEncryptXMLComponent
   ```
4. 修改job实现代码，override work方法，定制自己的任务处理逻辑，即旧版json-》新版json-》新版xml。

###### 如果以后不仅想用log4j写日志，还想采用自己开发的一个模块将日志输出到hdfs呢？
1. 新写一个日志输出层的组件，里面包含日志输出到hdfs的逻辑
2. 修改launch层实现Launcher代码，将1中的组件注入进去，比如
    ```scala
    with Log4jLoggerComponent
   ```
   修改成
   ```scala
    with Log4jLoggerComponent with HDFSLoggerComponent
   ```
3. 在需要输出日志到hdfs的地方，添加日志输出代码


### 总结
通过上面的讲解，大家应该已经学会了如何使用Cake模式在Scala中实现高逼格的依赖注入，而且认识到其强大的解耦能力和易扩展易维护的能力，
但是这个目的也只是一个浅层次的目的，会使用Cake模式不代表你就能设计出优秀的程序。
通过这个案例大家应该学会该如何设计自己的程序，小到一个模块、一个接口的设计，大到一个系统、一个平台的设计。
通过上面的过程大家有没有总结出我设计程序的要点呢？我先来总结几点，
1. 需求分析，这一步很重要，这一步错后面也许就要重构了
2. 思考如何设计可以让程序变得易扩展、易维护，当然还要考虑性能
3. 学会分层设计，复杂问题简单化
4. 尽量高层次的抽象
5. 将共通的逻辑都抽象到一处处理，只让下游实现定制化内容

具体代码可以从github获取：https://github.com/deanzz/json-transformer
