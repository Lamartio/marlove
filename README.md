# Introduction

Welcome to my example app that visualizes the Rijksmuseum API! The project is segregated in modules,
because:

- **Compilation times:** Gradle ony compiles modules that are changed
- **Scalability:** When a module grows, it got split. Thereby nothing gets so big that it becomes
  unmanageable.

## Everything is a lib

Mentally I consider all modules as libs. Just like any lib; a lib can have libs etc... Each lib has
limited public access (so mostly internal) so that there is no confusion in how to use each lib.
Each lib clearly defines its dependencies, which gives great overview and eases the decision whether
to use dependency injection (Dagger, Hilt, Koin, or just class delegation), but usually it is not
necessary to use DI at all.

NOTE: the UI is not yet split into its own module.

## Structural versus Logical

Code is split in code that just delegates and aggeregates other code and code that is actually doing
something. The prior I define as structural and the latter as logical. Since the logical is the one
doing something, this should be the one to be tested.

A good example of structural are the ViewModels. Basically they are just aggregating what the UI
requires. Therefore they do not require to be tested. Yet if a project requires full certainty, it
can be good to test them (get your green lights before publishing!).

## Code style

Mostly I use the style Kotlin already uses, that is:

- Rely on interfaces (IOP
- Instantiating is done through a function (listOf, mapOf etc...)
- ...or through an extension method (toList, toMap)
- Usually I write methods, but in some cases it could be useful to create a property holding a
  lambda. The benefit is that it is easier to pass around.

## :libs:domain

All that is within the 'domain' of this app is included in here. This is a good place for
project-wide utilities, or (as in the case of this app) models that are unambiguous through the
whole project.

## :libs:rijksmuseum

Internally this is connecting with an API, but for the one using the 'lib' it is just an interface
with some asynchronous/suspending actions. Hence it referenced as `val museum` in the rest of the
project. To proof API working, there are EndToEnd tests included.

## :libs:services

This is an Android library exposing all the device API in a way I want to use it within this domain.
In the case of this app it exposes a bridge between persistence and the domain-objects. To keep
things simple; the storage is just a SharedPreferences implementation (through DataStore), but when
the app grows, it would be beneficial to switch to an SqlLite variant like Room.

## :libs:optics

I favor to create a single immutable state for its testability and predictability. Initially I used
Arrow-kt Optics to do so, but it required `kotlin-kap` and too much libraries. Therefore, I just
wrote the parts I needed and explained them tests.

## :libs:logic

Converts the stateless interfaces like `RijksMuseum` and `Services.Storage` to single immutable
state. The state gets shared through a flow and updated through actions.

## :app

The actual compilable application. In it entry-point `RijksApplication` it instantiates all the
above. By this point, the application is only displaying State, calling actions and mapping State
into models that fit the UI.

# Backlog

| TODO | DOING | DONE |
| ---- | ----- | ---- |
| | | Discover the Rijksmuseum API: https://runkit.com/lamartio/621f1d2429367b00081238a4 (I will delete this a week after delivering)
| | | Setup a networking module
| | | Add a basic request
| | | Write an integration test
| | | Improve the returning value
| | | Get the details from Rijksmuseum
| | | Add a services module
| | | Add datastore
| | | Add a getter and setter for data
| | | Add a domain module for sharing unambiguous entities
| | | Persist something serializable
| | | Persist the collection and details
| | | Create a logic module
| | | Write a state composable mutation util based on Arrow-KT.
| | | Create a test suite that explains it's working
| | | Write some Async logic within a test
| | | Let Retrofit work with kotlinx.serialization: https://github.com/JakeWharton/retrofit2-kotlinx-serialization-converter
| | | Mutable can be improved to support, merging, concatting and exhausting
| | | Logic tests are dependant delay! Can not happen
| | | Add a viewModel
| | | Instantiate through ViewModelFactory
| | | Add some UI
| | | Migrate to Marlove
| | | Pagination
| |  Pull to refresh
| Clarify DI decisions
| Generalize and abstract storage
| URL in build config
| Centralize dependencies in BuildSrc (or Composite build)
| Improve error logging in network & storage
| Test cases for strategy
| Test cases for actions
| Test cases for the mappers of MainViewModel & .toStateDelegate()
| Standardize test naming to (Given...) When...Then
| Integrate KTlint, Detekt and/or Spotless 