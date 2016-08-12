[linkbait-plugin-api](../index.md) / [com.nishtahir.linkbait.plugin](index.md) / [DataSource](.)


# DataSource

`fun DataSource(type:&nbsp;[Type](-type/index.md), url:&nbsp;String, username:&nbsp;String, password:&nbsp;String): [DataSource](http://docs.oracle.com/javase/6/docs/api/javax/sql/DataSource.html)` [(source)](https://gitlab.com/nishtahir/linkbait/tree/master/linkbait-plugin-api/src/main/kotlin//com/nishtahir/linkbait/plugin/Datasource.kt#L13)

Factory method to provide database connection instances. This prevents a clash when
loading drivers using the Driver manager


### Parameters

`type` - See Type for supported db drivers

`username` - db user

`password` - db password


