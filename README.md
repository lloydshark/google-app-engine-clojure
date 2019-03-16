# Google App Engine Standard - Full Stack Clojure and ClojureScript

#### What is this?

A example of a full stack (clojure backend / clojurescript frontend) app for Google App Engine Standard (Java).

It includes the configuration for a hot-loading front end (via figwheel main), repl attached development of the backend
(via nrepl) and utilises the standard app engine dev server.

#### Quick Start

(the following was checked with Google Cloud SDK [234.0.0] & app-engine-java: [1.9.71]).

- You need Google Cloud SDK (with the app-engine-java installed)

See https://cloud.google.com/appengine/docs/standard/java/quickstart

- Clone this repo.

```git clone https://github.com/lloydshark/google-app-engine-clojure.git```

- Build and Run the DevServer.

```./scripts/build-and-run-dev-server.sh```

- Connect a Server Dev Repl

For Cursive:

Create a Remote Repl -> Connect to Server -> Host: localhost, Port: 8888

- Build and Run the Dev UI.

```./scripts/build-and-run-dev-ui.sh```

#### Why?

Google App Engine Standard is a great piece of technology for running your app.

But the project setup documentation is all based around gradle / maven so it can be hard to get started for Clojure.

And given previous limitations of the app engine tooling it was hard to get repl connected development running - but no more.

#### What's included ?

This example app uses:

- Clojure Tools Deps & the CLI
- Ring
- nRepl
- Figwheel Main
- Standard Google App Engine Tooling

#### How To

- Build and Deploy a release to app engine

```./scripts/build-and-deploy-release.sh```

- Add a new servlet

```Edit app-engine.edn if you wish to add more servlets.```

- Change the nrepl port

```Edit app-engine.edn if you wish to change the default nrepl port from 8888.```

#### TODO

- Provide a template?
- Unit Testing Server.
- Unit Testing UI.
- Logging.
- Add Re-Frame?
