---
layout: home
---

# Updating documentation

Notes on how to update the documentation...

## Setup instructions

Follow the instructions on Github skipping step one and three:
https://help.github.com/articles/setting-up-your-github-pages-site-locally-with-jekyll/

## Running the site locally

```bash
bundle exec jekyll serve
```

## Adding examples

0. Created a new markdown file in `_includes/examples` such as `circles.md`
0. Add a second level heading `## Circles`
0. Write example, including an image
0. Include the new example in `examples.md`: `{% raw %}{%{% endraw %} include examples/circles.md {% raw %}%}{% endraw %}`
