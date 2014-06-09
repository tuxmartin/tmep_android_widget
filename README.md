TMEP Android widget
===================

#### [Aplikace ke stažení](https://github.com/tuxmartin/tmep_android_widget/releases)


Pro použití přidejte na plochu widget.

Z důvodu úspory přenosu dat nemá widget automatickou aktualizaci.
Aktualizaci provedete ťuknutím na widget.

Pro změnu adresy teploměru spusťte aplikaci. V současné verzi není možné mít více widgetů pro ruzné teploměry.

##### Informace pro správce webů s TMEP aplikací. 
TMEP od verze z [29.12.2013](https://github.com/MultiTricker/TMEP/commit/1759082dc19c56a202be7343c53fee01f77b2149) pouziva pro pristup k databazi MySQLi, místo MySQL které bylo předtím.
Pokud instalujete nový TMEP, nebo aktualizujete starý vše bude fungovat.

Pokud máte v provozu starou verzi TMEPu a nechcete aktualizovat, nakopírujte do rootu aplikace soubor [vystup-json.php](https://github.com/tuxmartin/tmep_android_widget/blob/master/ostatni/vystup-json.php). Získate tak JSON výstup kompatibilní s novu verzí TMEP bez nutnosti aktualizace. Pro kontrolu přejděte na adresu *http://adresa.vaseho.teplomeru.tld/vystup-json.php*, kde by mel byt vytup v JSON formatu.



