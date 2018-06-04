# Installation

--------------

## Preparing Docker environment

First you need to have [Docker](https://docs.docker.com/install) installed on your system.  

Then run docker or create and start docker machine.

--------------

## Clone the repository

Open a command prompt and use the following commands to clone repository and navigate to project directory:

```bash
$ git clone https://github.com/nokia-wroclaw/innovative-project-jackdaw.git
$ cd innovative-project-jackdaw
```

--------------

## Run

Run docker-compose

```bash
$ docker-compose up
```

or 

```bash
$ ./run.sh
```

--------------

## Stop

```bash
$ docker-compose down
```

or in order to clear Jackdaw images:

```bash
$ ./clear.sh
```

-------------

If everything went right, you should be able to access:

* map visualization at `localhost:3001`

* charts visualization at `localhost:3000`
