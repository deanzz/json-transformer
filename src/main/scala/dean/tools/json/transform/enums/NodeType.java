package dean.tools.json.transform.enums;

public enum NodeType {

    NULL, //(0)

    //
    DATA_SOURCE, //(1),

    //关联数据源
    DATA_CORRELATION, //(2),

    //API, //(e.g:微博数据)
    DATA_API, //(3),

    //数据类型修改
    DATA_Type_MODIFY, //(4),

    //删除数据列
    DATA_COLUMN_DELETE, //(5),

    //删除数据行
    DATA_ROW_DELETE, //(6),

    //替换数据值
    DATA_VALUE_REPLACE, //(7),

    //old:数据列的分组
    //new:column split
    COLUMN_SPLIT, //(8),

    TOPN, //(9),

    //特征选取
    FEATURE_SELECT, //(10),

    //选择Label
    SELECT_LABEL, //(11),

    /*
    //基于滤波器的特征选择
    FILTER_BASED_FEATURE_SELECTION , //(),

    //线性判别分析
    FISHER_LINEAR_DISCRIMINANT_ANALYSIS , //(100000),

    //PERMUTATION_FEATURE
    PERMUTATION_FEATURE , //(100000),
    */

    //模型预测
    DATA_MODEL_FORECAST, //(12),

    //13号  没有   设为null
    NULL13,

    //模型对比
    DATA_MODEL_COMPARE, //(14),

    //智能分析
    INTELLIGENCE_ANALYSIS, //(15),

    //选择测试数据
    TEST_DATA_SELECT, //(16),

    //逻辑回归
    LOGISTIC_REGRESSION, //(17),

    //决策树
    DECISIONTREE, //(18),

    //svm
    SVM, //(19),

    //朴素贝叶斯
    NAIVEBAYES, //(20),

    //knn
    KNN,

    //(21),

    //
    COLUMN_CALCULATE,//22 column calculate

    ID_COLUMN,//23 选择id列

    //新增加的列的名称修改
    DATA_COLUMN_NAME_MODIFY,//24

    MISS_DATA_FILL,//25 缺失数据的填充

    CLEARING_REPEAT_COLUMN,//26清除重复数据列

    COPY_COLUMN,//27复制数据列

    K_MEANS,//28 K-Means

    LINEAR_REGRESSION,//29 线性回归

    //integration module used and not register in moudleInfo table
    CROSSVAILIDATION, //30 cross vailidataion 模块

    ONEHUNDRED,//31 计算一百遍 模块

    APPEND_DATA_ROW_COL,//32 增加数据行或数据列

    TFIDF_TEXT_EXTRACT, //33 TFIDF文本提取

    ANNCLASSIFIER_ONLY,//34 神经网络单独算法

    LOGISTIC_REGRESS_ONLY,//35逻辑回归单独算法

    LINEAR_REGRESS_ONLY,//36线性回归单独算法

    SVM_ONLY,//37svm单独算法

    //决策树单独算法
    DECISIONTREE_ONLY, //(38),

    //随机森林单独算法
    RANDOMFOREST_ONLY, //(39),

    //决策树回归单独算法
    //integration module
    INTEGRATION, //(40)

    NULL41, //(41)
    NULL42, NULL43, NULL44,

    DECISIONTREE_REGRESSION_ONLY, //(45),

    //随机森林回归单独算法
    RANDOMFOREST_REGRESSION_ONLY, //(46),

    //Gradient-boosted tree(梯度提升树单独算法)
    GRADIENTBOOSTEDTREE_CLASSIFIER_ONLY,//(47)

    //Gradient-boosted tree(梯度提升树回归)
    GRADIENTBOOSTEDTREE_REGRESSION_ONLY,//(48)

    //AFT生存分析算法
    AFTSURVIVAL_ANALYSIS_REGRESSION_ONLY,//(49)

    //日志模块
    LOG_DATA_MODULE,//50

    //基于模型推荐算法
    COLLABORATIVE_FILTERING,//51

    //指定某数据源的一列为“压缩向量”
    SPARSE_VECTOR, //52

    //推荐器预测
    COLLABORATIVE_FILTERING_PREDICT, //53

    //one-vs-rest
    //integration module used and not register in moudleInfo table
    ONEVSREST_CLASSIFIER_ONLY, //54

    //地址转换
    ADDRESS_TRANSFORM, //55

    //异常值检测
    ANOMALOUS_FEATURE_DETECTION, //56

    //基于项目推荐算法
    ITEM_BASED_CF,//57

    //PCA
    PCA,//58

    //PCAPredict
    PCA_PREDICT,//59

    //Cognition
    COGNITION,//60

    IG,//61

    FPGrowth,//62

    STANDARD,//63

    NORMALIZER,//64

    STANDARD_PREDICT,//65

    AssociationRuleFilter, //66   关联规则过滤

    DYNAMIC_FILE_SAVE,//67

    DYNAMIC_FILE_UPDATE,//68

    STRATIFIED_SAMPLING, //69
    DISCRETIZATION, // 70 数据离散化
    XGBOOST_LOGISTIC,//71

    COLUMN_MERGE, //72  列合并

    DATA_SORTED, //73  数据排序

    STRING_REPLACE, //74  字符处理

    F_TEST, //75,

    KNN_CLASSIFICATION_ONLY, //76 KNN

    NAIVE_BAYES_CLASSIFICATION_ONLY, //77 naive bayes

    T_TEST_INSPECTION, //78 Ttest检验

    CHI_SQ_TEST, //79 卡方检验

    KS_NORM_TEST, //80 KS正态检验

    T_SNE,// 81 t-sne

    DATA_SCHEMA_MODIFY,// 82 新的类型转换模块，增加输出被转换后置空的行数据

    COLUMN_ENCRYPT,// 83 列加密

    WORD2VEC,// 84

    WORD2VEC_PREDICT,// 85

    TEXTWORDCOUNT,//86

    COGNITION_STATISTICS,//87 cognition statistics

    COGNITION_CORRELATION,//88 cognition correlation

    COGNITION_CROSS_DISTRIBUTION,//89 cognition crossDistribution

    SEGMENTATION_NLP, //90

    TFIDF_TEXT_EXTRACT_SECOND, //91 TFIDF second,文本提取

    KS_NORM_TEST_PERFECT,//92 ks正态检验(支持手动输入均值和方差的方式)

    FPGrowth_RECOMMEND,//93  关联规则推荐

    SVD,//94

    LDA,//95  LDA

    Bisecting_K_MEANS, //96 分层聚类之二分k-means

    CCOLUMN_TRANSFORM_ROW,//97 行列转换

    LDA_PREDICT,//98  LDA预测

    MULTICLASS_LOGISTIC_REGRESS_ONLY, // 99 多元逻辑回归

    MULTICLASS_NAIVE_BAYES_CLASSIFICATION_ONLY, // 100 多元朴素贝叶斯

    MULTICLASS_DECISIONTREE_ONLY, // 101, 多元决策树

    MULTICLASS_RANDOMFOREST_ONLY, // 102  多元随机森林

    IG_SECOND,//103

    CHI_SQUARED, //104

    CLEARING_REPEAT_COLUMN_SECOND, //105

    JOIN_NEW,//106

    SIMILARITY,//107

    ROW_TRANSFORM_COLUMN,//108

    MATCHING,//109

    STRATIFIED_SAMPLING_NEW, //110 新抽样

    VARIABLE_ASSIGNMENT,//111 变量赋值

    //112 版本聚合
    SNAPSHOTS_AGGREGATION,

    CASE_CONVERSION,//113 大小写转换

    ;
}
