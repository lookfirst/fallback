fallback
========

fallback provides a nice example web application archive (war) for
integrating Spring / Hibernate / JMX / JPA / Ehcache.

In order to come up with this clean of an integration, there is a ton of
conflicting blog postings and documentation you would have to sift
through. My goal here is to do that work for you and provide a nice
basis for starting from. The project is up on github in the hopes that
you will fork it and make improvements yourself.

The fallback project itself is a very basic 3-tier web application with
a RESTful servlet frontend that takes a request and calls a method on 
a bean which contains the business logic. If you are coming from EJB3 
experience, this will look very familiar. Annotations are used as much
as possible to simplify the Spring configuration.

Database Access
---------------
By example, access to the database is managed through the standard
DAO/Entity pattern. There is also an example of how to use an
EntityManager if you want to go that route instead. The `BaseDAO`
implementation enables hibernate query caching with Ehcache. Basic
transactions are also fully supported in the business logic bean with by
adding a `@Transactional` annotation. Enabling JTA is possible, but out
of the scope of this project.

JMX
---
For JMX, the `JmxAgent` class registers both Ehcache and Hibernate with
the `MBeanServer` that is setup through Spring. Using the
`@ManagedResource` `@ManagedOperation` and `@ManagedAttribute`
annotations on a bean automatically exposes classes, methods and
properties via JMX. No need for XML configuration.

Build System
------------
The build system for this project doesn't use Maven. Instead we use a
more simple solution called
[Sweetened](http://sweetened.googlecode.com). All of the jars that the
project needs are located in the lib directory. This ensures that over
time the project can be built regardless of the state of it. Sweetened
is also responsible for building the Eclipse .classpath and .project files.

There you have it. Please poke around the code and if you have questions
feel free to ask me.

Getting Started
---------------

1. Clone this project: git clone 
2. Install tomcat6 somewhere
3. cd fallback; echo "tomcat.dir=/path/to/tomcat6" > user.properties
4. ant .eclipse

The .eclipse target will generate a .project and .classpath file
for you so that you can load it easily into Eclipse.

1. Right click in the Project Explorer
2. Import...
3. Import existing project into workspace
4. Select the fallback directory.
5. Go

Now build the project:

    ant all

This will copy an exploded war directory into your tomcat6 webapps
directory.

### MySQL

Before you can start up Tomcat, you need a MySQL database called
'fallback' and accessible on localhost by user root with no password.

If you want to change any of this before you start up Tomcat, just edit
the database connection settings in the `web/WEB-INF/applicationContext.xml` 
file. The `persistence.xml` file is configured to auto create the table and
all of the columns.
