#导言：

在微服务结合前端AJAX越加普遍的今天，以一种没有后端的web开发理念，极大缩小了开发成本，此博客结合这个概念做了一个数据服务原型。

主要有如下特色：
* 用户只需以JSON格式定义表结构，以所见即所得的方式快速创建、储存数据
* 工具提供CRUD API，以httprequest对数据进行增删改查原子操作,可省去大量时间写DDL或DAL。
* API兼容ExtJS的跨域Ajax请求，可以完全舍弃后端，客户端Ajax在回调中直接处理返回值的前台UI业务逻辑，

调用例子如下：

    Ext.data.JsonP.request({

        url: 'http://192.168.81.33:8090/datastore/do/'+Ext.getCmp('Main').DB+'/'+record.raw.name+'/findall/',
    
        callbackKey: 'datastore_callback',
    
        success: function(response){
    
            //do something
    
        },   
    
        failure: function(response){
    
            //do something
    
        }
    
    });


##创建表：
以JSON字符串作为输入，以所见即所得的方式创建表结构及其初始数据集




##API服务：

###增
* Request URL:          http://[host]:[port]/datastore/do/[db name]/[table name]/save
* Supported method: GET or POST
* Request params:•data(required):  record in json format,each records should be separate with ‘;’ if has more than 1 record. * For instance: {"name":"gnail","gender":false} OR {"name":"gnail","gender":false};{"name":"nehc","gender":true}
* Response json:
      {
    
          “Code”:0,            //0成功，1失败
    
          “Msg”:”success” //or “failure”
    
      }

###删
* Request URL:          http://[_host_]:[_port_]/datastore/do/[_db name_]/[_table name_]/delete
* Supported method: GET or POST
* Request params:•condition (required):  same as find API
* Response json:

      {

          “Code”:0,           //0成功，1失败

          “Msg”:”success” //or “failure”

      }

###改
* Request URL:          http://[host]:[port]/datastore/do/[db name]/[table name]/update
* Supported method: GET or POST
* Request params:   condition (required): same as find API
* set (required):   string as “key=value” format,if has more than 1 setted value, separated with “;”. For instance:  key1=value1 OR key1=value1;key2=value2
* Response json:

      {

          “Code”:0,           //0成功，1失败

          “Msg”:”success” //or “failure”

      }

###查全部
* Request URL:  http://[host]:[port]/datastore/do/[db name]/[table name]/findall
* Supported method: GET or POST
* Request params: no param
* Response json:    array object contains all returned object

###查
* Request URL:          http://[host]:[port]/datastore/do/[db name]/[table name]/find
* Supported method: GET or POST
* Request params:•condition:        this is string ontains 1 or more than 1 condition expression,separating with “;” if set several conditions. For instance name=="liangchen" OR name=="liangchen";gender==true


Operator Category in expresson:
* ==     for instance, name=="liangchen"
* >       for instance, age>60
* <       for instance, age<60
* >=     for instance, age>=60
* <=     for instance, age<=60
* !=      for instance, age!=60
* in      for instance, age in (20,30)
* nin    for instance, age nin (20,30)

* Response json:  array object contains all returned object

###删除表
* Request URL:          http://[host]:[port]/datastore/do/[db name]/[table name]/drop
* Supported method: GET or POST
* Request params: no param
* Response json:

      {

          “Code”:0,           //0成功，1失败

          “Msg”:”success” //or “failure”

      }

