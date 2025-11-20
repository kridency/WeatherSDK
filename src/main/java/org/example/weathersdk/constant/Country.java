package org.example.weathersdk.constant;

public enum Country {
    SQ("sq"),
    AF("af"),
    AR("ar"),
    AZ("az"),
    EU("eu"),
    BE("be"),
    BG("bg"),
    CA("ca"),
    ZH_CN("zh_cn"),
    ZH_TW("zh_tw"),
    HR("hr"),
    CZ("cz"),
    DA("da"),
    NL("nl"),
    EN("en"),
    FI("fi"),
    FR("fr"),
    GB("gb"),
    GL("gl"),
    DE("de"),
    EL("el"),
    HE("he"),
    HI("hi"),
    HU("hu"),
    IS("is"),
    ID("id"),
    IT("it"),
    JA("ja"),
    KR("kr"),
    KU("ku"),
    LA("la"),
    LT("lt"),
    MK("mk"),
    NO("no"),
    FA("fa"),
    PL("pl"),
    PT("pt"),
    PT_BR("pt_br"),
    RO("ro"),
    RU("ru"),
    SR("sr"),
    SK("sk"),
    SL("sl"),
    SP("sp"),
    SV("sv"),
    TH("th"),
    TR("tr"),
    UA("ua"),
    VI("vi"),
    ZU("zu");

    private final String country;

    Country(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return country;
    }
}
