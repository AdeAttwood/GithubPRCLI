<div align="center">

# Github Pull Request CLI

</div>

Creating a pull request from your favourite `EDITOR`. A simple wrapper around
the `gh` CLI tool to craft a pull request using your project's template. The
idea is that when you are working on a PR, you create better, descriptive PRs
if you `prepare` them as you go. If you wait until the end of your work, you
will probably miss out all the little details that will explain *why* to your
reviewers.

## Pre-requisites

Before you begin, you must have your `EDITOR` set up in your shell. Some
examples could be one of the following your shell init:

```sh
export EDITOR="nvim"
export EDITOR="emacsclient --alternate-editor 'emacs --daemon && emacsclient"
export EDITOR="code --wait"
```

## Installation

The only way to install this currently is to install it from source. You also
have two installation methods, as a standalone CLI or as a `gh` extension. The
tool is written in Clojure and uses the GraalVM to compile a native binary.

1) Download the repo

```sh
git clone https://github.com/AdeAttwood/GithubPRCLI.git
```

2) Build the binary

The most simple way to build is to use docker. If you have the `clojure`
toolchain installed, please refer to the [Makefile](./Makefile) for more info

```sh
make build-in-docker
```

3.a) Install the binary

You only need to run this step if you want to use it as a standalone binary.

```sh
cp ./target/ghpr-standalone ~/.local/bin/ghpr
```

3.b) Install the `gh` extension

You only need to run this step if you want to use it as a `gh` plugin. It will
use the make `mr` (Merge Request) as the extension name, because `pr` has
already been taken.

```sh
make gh-extension
cd target/gh-mr
gh extension install .
```

## Usage

During your development work, you can run the `prepare` command. If this is the
first time you are preparing a PR, it will take a fresh copy of your projects
`.github/pull_request_template.md` for you to use. If your project does not
have one, then it will use an empty file.  This command can be run as much as
you like, each iteration will be building on the previous run.

> **Note**:
> If you are using the `gh` extension, you can swap out `ghpr` for `gh mr`

```sh
ghpr prepare
```

Once your PR is ready, you can `submit` it. This will use the `gh pr create`
command under the hood, adding all the flags to use the output of `prepare` as
the body.

```sh
ghpr submit
```
