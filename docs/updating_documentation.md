---
layout: home
---

# Updating documentation

Notes on how to update the documentation...

## Setup instructions

Open Terminal.

Check whether you have Ruby 2.1.0 or higher installed:

```bash
$ ruby --version
ruby 2.X.X
```

If you don't have Ruby installed, install [Ruby 2.1.0 or higher](https://www.ruby-lang.org/en/downloads/).

Install [Bundler](http://bundler.io/):

```bash
$ gem install bundler
$ cd docs
$ bundle install
```

## Running the site locally

```bash
$ bundle exec jekyll serve
```

## Adding examples

0. Created a new markdown file in `_includes/examples` such as `circles.md`
0. Add a second level heading `## Circles`
0. Write example, including an image
0. Include the new example in `examples.md`: `{% raw %}{%{% endraw %} include examples/circles.md {% raw %}%}{% endraw %}`
