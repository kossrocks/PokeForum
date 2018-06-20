# Welcome to PokéForum!


### The team behind PokéForum:

* Jakob Arneitz 
* Nadine Neumann 
* Mathias Pützl 
* Thomas Rinnhofer

### Workload Distribution:

* Jakob Arneitz: co-responsible for attack and pokemon page

* Nadine Neumann: responsible for design (Bootstrap, CSS), co-responsible for user registration

* Mathias Pützl: project leader and responsible for creating and maintaining models, co-responsible for user registation,
                 responsible for 

* Thomas Rinnhofer: co-responsible for attack, user and pokemon page

### How to setup PokéForum

1. First step: download the project source from [here] (https://www.github.com)
2. Open Eclipse
3. Create a New Dynamic Web Project in Eclipse
4. Convert the project to a Maven Project
5. Import the downloaded source
6. Go to the src folder and open db.properties --> setup your database connection like this 
   with the login credentials that were send to your per email:
     * db.url=jdbc:mysql://`<yourServerIP>`/`<yourDatabase>`
     * db.username=username
     * db.password=password
7. Be sure that you set up your Eclipse project (Server, Runtime, ...)
8. Publish project to Tomcat and start Tomcat(8.5) umschreiben
9. Open the Web Application with this [link] (http://localhost:8080/test)
10. To get some users and testdata you have to extend the above link with /fillUsers
11. To login use the usernames and password that were send to you per email
12. Have fun with PokéForum!

The use with Chrome is not recommended.

Nicht mit Chrome kompatibel 
