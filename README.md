# Modular Monolith

This repository is aimed to be a somewhat realistic approach on how several teams can work in
one code-base. The goal is to apply some lessons learnt from a microservice architecture 
to a monolithic architecture.
The aim of this example project is to keep the architecture as simple as possible while achieving 
a maximum of isolation between the code of the teams as well as the different modules.
In the end, it doesn't matter though if you go for a simple layered architecture or a more
sophisticated onion architecture. This project setup as well as the rules will fit any.

Gradle is used to avoid any transitive dependencies and hide dependencies as much as possible.
In general, this setup can be replicated with any language and any build module which supports a 
strong isolation and hiding of transitive dependencies.

# Table of contents
- [Rules for a modular monolith](#rules-for-a-modular-monolith)
- [Use case for this example project](#use-case-for-this-example-project)
  - [Setup](#setup)
    - [High-level overview](#high-level-overview)
    - [Dependency definitions](#dependency-definitions)
    - [Keeping it simple](#keeping-it-simple)
- [FAQ](#faq)
- [Run the project](#run-the-project)
  - [Intellij Idea](#intellij-idea)
  - [Gradle](#gradle)


## Rules for a modular monolith
- Every outgoing connection needs to be wrapped in circuit breakers[^1] and fallbacks. Teams agree
on SLAs and fallbacks
- Every outgoing call needs to go through some defined adapters. We'll borrow this concept from
the ports and adapters architecture[^2]
- Every module defines its own dependencies and configs and keeps them as encapsulated as possible
- Every module has a clear owner. Other teams can participate, but their code needs to be isolated
- Every other team is treated as foreign code and service, as if it was another microservice
- Dependencies in a module should never be transitively exposed to other modules
- View dependencies as part of your responsibility and think about them actively
- Classes should be package private by default. One could e.g. cut the packages by domain or by feature

## Use case for this example project
The exemplary use case for this repository is there are two teams contributing to this application.
The "app" team as well the "ai" team. While the "app" team owns this code and both services, the
"ai" team should be able to contribute to this code to enhance certain features from their perspective.
The agreement is to use Spring Boot, but the used dependencies should be flexible per team and per deployable.

Further, the user-facing deployable makes use of prepared data from the internal deployable. Meaning,
the internal deployable receives data and transforms and enhances it for the user-facing deployable to use.

### Setup

#### High-level overview
- The "app" team owns this repository as well as the internal ([indexation](indexation)) and 
external ([app](app)) deployable
- The "ai" team contributes to these deployables by using certain ai features ([ai](ai)). This would include
  calling their microservice(s) not in this repository as well as adding business logic 
- The internal deployed application follows an event-driven approach and shares the persisted entities
  ([entities](indexation/entities)) with the user-facing services for convenience
- The external deployed service follows a classic non-reactive layered architecture approach

#### Dependency definitions
- The dependencies defined in the respective gradle files in [buildSrc/src/main/kotlin](buildSrc/src/main/kotlin)
should be carefully defined. Only absolutely necessary dependencies should be defined on global levels
- We define Spring Boot Gradle plugin as well as the Spring Boot dependency management plugin 
 on a global level in [buildSrc/src/main/kotlin/modular_monolith.java-common-conventions.gradle.kts](buildSrc/src/main/kotlin/modular_monolith.java-common-conventions.gradle.kts).
**This definition makes not assumption about any used Spring modules (Web, WebFlux, Data, ...)!** 
These dependencies should be defined in their respective modules
- As we defined Spring as common framework, we define Spring Context as common dependency 
in [buildSrc/src/main/kotlin/modular_monolith.java-library-conventions.gradle.kts](buildSrc/src/main/kotlin/modular_monolith.java-library-conventions.gradle.kts)
so every module can make easily use of Sprint annotations

#### Keeping it simple
For the module [entities](indexation/entities) if we stick to a strict and correct isolation, we would have to
duplicate the entities in both modules ([indexation](indexation)) and ([app](app)). While this is certainly the
more correct way, we decided to go for a more pragmatic approach. Depending on the complexity of the app, this
should not be done.

## FAQ
> Where should I put common domain logic? 

You could create a new module called "domain" and let all modules use it. The "domain" module should 
    not have any dependencies on any other module. To avoid the domain module becoming a dumping ground for
all sorts of things ("util" package) we advise to think carefully whether it's needed.
<br/><br/>

> Why not common configuration module? 

Each deployable acts as independent service, as if they were microservices. In the end they might or
  might not use the same data sources. We favour duplication over DRY here for better isolation.<br/><br/>

> Why doesn't the indexation module have any circuit breakers?

With this example we wanted to show a rather simple and trivial module. If it was a more business critical
and bigger module, it should absolutely have circuit breakers.<br/><br/>


> Why explicitly scanning the base packages in the app module?

We could also put all the classes in the same package. With a monolith and using package private classes
as default it makes sense though to give every team the biggest flexibility 
as well as hiding their classes.<br/><br/>

> Why the Spring Boot plugin dependency in the common dependencies?

As it was decided for this example project to use Spring Boot, we can use this dependency
to ensure a consistent version of all Spring Boot related dependencies in all modules.<br/><br/>

## Run the project
### Intellij Idea
In intellij you might encounter an issue where Idea does not find all the split up config files. Run a gradle
build in that case.

### Gradle
`./gradlew bootRun`

[^1]: https://resilience4j.readme.io/
[^2]: https://alistair.cockburn.us/hexagonal-architecture/