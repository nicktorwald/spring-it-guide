# A short guide how to cook integration tests

This is an additional repo that is used for demonstration purposes at SibEdge.

## How to use this guide

This tutorial evolves within a number of commits where each commit improves the solution in terms of performance,
maintainability, and security.

To try it out, check out a particular commit from the `master` branch. It will roll back you to the needed evolution
state.

## Prerequisites

We've developed yet another service which requires a PostgreSQL instance to store and retrieve quotations. We want to
provide integration tests to be sure all the stuff is OK.

As the start point, we have a test environment where the required services are installed.

### Props:

- closest to a production usage
- trustworthy in terms test results

### Cons:

- sluggish
- dependent on pre-configured external services
- hard supported (multi-usage, updates, dynamic deployments and so on)

## Evolution 0

Avoid any integrations with external services. Use *test doubles* (mock/fake/stub objects) to simulate behaviour of the
target services.

```shell
$ docker image build -t nicktorwald/quotation-service:evol0 .
$ docker container run --rm -it nicktorwald/quotation-service:evol0 verify
```

### Props:

- fastest
- autonomous (free of any external dependencies)

### Cons:

- untrustworthy (it doesn't show a real picture)
- fully incompatible with the target environment
