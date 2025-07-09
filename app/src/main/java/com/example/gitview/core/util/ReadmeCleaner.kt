package com.example.gitview.core.util

fun cleanReadme(raw: String): String {
    return raw
        // Remove triple backtick code blocks
        .replace(Regex("```.*?```", RegexOption.DOT_MATCHES_ALL), " ")

        // Remove inline code `...`
        .replace(Regex("`.*?`"), " ")

        // Remove HTML tags like <img src=...> or <a href=...>
        .replace(Regex("<[^>]*>"), " ")

        // Remove markdown image links ![alt](url)
        .replace(Regex("!\\[.*?]\\(.*?\\)"), " ")

        // Remove standard markdown links [text](url)
        .replace(Regex("\\[.*?]\\(.*?\\)"), " ")

        // Remove reference-style link definitions: [label]: url
        .replace(Regex("^\\[.*?]:.*$", RegexOption.MULTILINE), " ")

        // Remove stray brackets and parentheses
        .replace(Regex("[\\[\\](){}]"), " ")

        // Remove markdown bullets, headings, and emphasis
        .replace(Regex("[*_#>~-]"), " ")

        // Collapse multiple whitespace characters into one
        .replace(Regex("\\s+"), " ")
        // Remove leftover exclamation marks and short standalone words (e.g. 'badge', 'animation')
        .replace(Regex("\\b(badge|animation|instructions|handle|image)\\b", RegexOption.IGNORE_CASE), " ")
        .replace(Regex("!"), " ")

        // Trim and optionally shorten to OpenAI-safe size
        .trim()
        .take(3500)
}