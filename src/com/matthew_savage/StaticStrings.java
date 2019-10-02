package com.matthew_savage;

public enum StaticStrings {
    ERR_HTML("Manganelo.com has modified it's codebase! Cupcaked Reader will not be able to get new content or updates to existing content until this has been addressed!"),
    ERR_DL_MISC("Something terrible has happened. I have no idea what that something is of course, but what I do know is nothing is downloading."),
    DL_PART(" is in the process of downloading but is available to begin reading."),
    DL_DONE(" has completed downloading!"),
    CAT_NOT_COLLECTED("Not Collected"),
    CAT_COLLECTED("Collected"),
    CAT_COMPLETED("Completed"),
    CAT_UNSURE("Undecided"),
    CAT_REJECTED("Rejected"),
    STAT_PRE_TITLE_TOT("My reader has "),
    STAT_SUF_TITLE_TOT(" total manga titles."),
    STAT_PRE_READING_TOT("I'm presently reading "),
    STAT_SUF_READING_TOT(" manga titles."),
    STAT_PRE_FIN_TOT("I've finished reading "),
    STAT_SUF_FIN_TOT(" manga titles."),
    STAT_PRE_PAGES_TOT("I've read "),
    STAT_SUF_PAGES_TOT(" pages of manga."),
    STAT_PRE_FAVE_TOT("I've favorited "),
    STAT_SUF_FAVE_TOT(" sublime manga titles."),
    STAT_PRE_BL_TOT("I've blacklisted "),
    STAT_SUF_BL_TOT(" dreadful manga titles."),
    STAT_PRE_PAGES_DAY("I read an average of "),
    STAT_SUF_PAGES_DAY(" pages per day."),
    STAT_PRE_GEN_ONE("My favorite genre is "),
    STAT_PRE_GEN_TWO("My second favorite genre is "),
    STAT_PRE_GEN_THREE("My third favorite genre is "),
    DB_NAME_SETTINGS("usr.db"),
    DB_NAME_MANGA("main.db"),
    DB_NAME_DOWNLOADING("downloads.db"),
    DB_TABLE_AVAILABLE("available_manga"),
    DB_TABLE_COMPLETED("completed"),
    DB_TABLE_READING("currently_reading"),
    DB_TABLE_HISTORY("history"),
    DB_TABLE_DOWNLOAD("downloading"),
    DB_TABLE_NOT_INTERESTED("not_interested"),
    DB_TABLE_UNDECIDED("undecided"),
    DB_TABLE_BOOKMARK("bookmark"),
    DB_TABLE_NEW_CHAPTERS("new_chapters"),
    DB_TABLE_STATS("stats"),
    DB_TABLE_VERSION("version_number"),
    DB_COL_ID("title_id"),
    DB_COL_TITLE("title"),
    DB_COL_AUTH("authors"),
    DB_COL_STATUS("status"),
    DB_COL_STATUS_VAL_DONE("Completed"),
    DB_COL_STATUS_VAL_NOT_DONE("Ongoing"),
    DB_COL_SUMM("summary"),
    DB_COL_URL("web_address"),
    DB_COL_GENRE("genre_tags"),
    DB_COL_CHAP_TOT("total_chapters"),
    DB_COL_CUR_PAGE("current_page"),
    DB_COL_LAST_CHAP_READ("last_chapter_read"),
    DB_COL_LAST_CHAP_DL("last_chapter_downloaded"),
    DB_COL_NEW_CHAP_BOOL("new_chapters"),
    DB_COL_ENT("entry_number"),
    DB_COL_FAVE_BOOL("favorite"),
    DB_VER_COL_VER("version"),
    VER_NUM_CURRENT("3.0"),
    DB_STATS_COL_TITLE_TOT("titles_total"),
    DB_STATS_COL_READING_TOT("reading_total"),
    DB_STATS_COL_FIN_TOT("finished_reading"),
    DB_STATS_COL_PAGES_TOT("pages_read"),
    DB_STATS_COL_FAVE_TOT("favorites"),
    DB_STATS_COL_BL_TOT("blacklisted"),
    DB_STATS_COL_PAGES_DAY("pages_daily"),
    DB_STATS_COL_GEN_ONE("first_genre"),
    DB_STATS_COL_GEN_TWO("second_genre"),
    DB_STATS_COL_GEN_THREE("third_genre"),
    DB_ATTACHED_PREFIX("main."),
    DB_ATTACHED_AVAILABLE("main.available_manga"),
    DB_ATTACHED_READING("main.currently_reading"),
    DB_ATTACHED_DOWNLOADING("downloadDb.downloading"),
//    DIR_ROOT (GetApplicationPath.getPath()),  //live dir
    DIR_ROOT("C:\\Users\\Misery Inc\\Documents\\Cupcaked Reader\\environment"),  //development dir
//        DIR_ROOT  ("C:\\Users\\Apple\\Cupcaked Manga Reader"),  //development dir
    DIR_MANGA("manga"),
    DIR_DB("data"),
    DIR_THUMBS("thumbs"),
    URL_ROOT("https://manganelo.com/"),
    URL_SEARCH_TITLE("search/"),
    URL_SEARCH_AUTHOR("search/");

    private final String value;

    StaticStrings(final String valueString) {
        this.value = valueString;
    }

    public final String getValue() {
        return value;
    }
}