import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReiScraper {
    public static String getPrice(String link) throws IOException {
        String price = "Unable to find price";
        Document doc = Jsoup.connect(link).get();
        Elements scripts = doc.select("script");
        Pattern pricePattern = Pattern.compile("'displayPrice':'(\\d+\\.\\d+)'");
        for (Element script: scripts) {
            Matcher priceMatcher = pricePattern.matcher(script.html());
            if (priceMatcher.find()) {
                price = priceMatcher.group(1);
            }
        }
        return price;
    }
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.rei.com/c/backpacking-packs").get();
        Elements links = doc.select("a[href]");
        List<Element> filteredLinks = links.stream().filter(link -> link.text().contains("Pack") && link.text().contains("reviews"))
                .collect(Collectors.toList());
        List<String> productLinks = filteredLinks.stream().map(link -> "https://www.rei.com" + link.attr("href")).collect(Collectors.toList());
        for (Element link : filteredLinks) {
            System.out.println(link.attr("href"));
        }
        for (String link : productLinks) {
            System.out.println(getPrice(link));
        }
    }
}
