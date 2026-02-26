# Wiki Scraper

## Project Description

A Python CLI tool that scrapes Wikipedia-style pages to generate summaries,
count words, extract tables, and compare article word frequencies with
language-level distributions. It is configuration-driven (base URL, language,
CSS selectors), supports caching, and outputs both tables and charts for
analysis.

Repository: https://github.com/abryndza/wiki-scraper

## Key Features

-   **Multi-command CLI:** Summary, word counting, table extraction, frequency
    analysis, and recursive word counting across linked pages.
-   **Config-driven scraping:** Works with custom wiki instances via
    `config.json` settings (base URL, selectors, language).
-   **Data outputs:** Saves counts and tables to local files and can generate
    charts for word frequency comparisons.
-   **Caching and logging:** Optional caching layer plus rich console output.
-   **Tested core logic:** Unit tests cover word counting and search query
    normalization.

## Project Structure

-   `src/wiki_scraper/`: CLI commands, scraping services, and infrastructure.
-   `tests/`: Unit tests for domain logic.
-   `config.json`: Runtime configuration (base URL, language, selectors).

## Usage

Examples (from the external repository):

```bash
poetry install
poetry run scraper --summary "Python"
poetry run scraper --count-words "Python"
poetry run scraper --table "Python" --number 1
poetry run scraper --analyze-relative-word-frequency --mode article --count 20
```

## What I Learned

-   Designing a modular CLI with dependency injection and clear ports/adapters.
-   Building reliable web scraping pipelines with parsing, caching, and error
    handling.
-   Turning scraped data into structured outputs and visualizations.
