package zad1;

public class Topic {
    public String nameOfTopic;
    public String themeOfTopic;
    public String dateOfPublication;
    public String authorPseudo;
    public String content;

    public Topic(String nameOfTopic, String themeOfTopic, String content, String authorPseudo, String dateOfPublication) {
        this.nameOfTopic = nameOfTopic;
        this.themeOfTopic = themeOfTopic;
        this.content = content;
        this.authorPseudo = authorPseudo;
        this.dateOfPublication = dateOfPublication;
    }

    public void setNameOfTopic(String nameOfTopic) {
        this.nameOfTopic = nameOfTopic;
    }

    public void setThemeOfTopic(String themeOfTopic) {
        this.themeOfTopic = themeOfTopic;
    }

    public void setDateOfPublication(String dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    public void setAuthorPseudo(String authorPseudo) {
        this.authorPseudo = authorPseudo;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Topic:" + nameOfTopic;
    }

    public String getNameOfTopic() {
        return nameOfTopic;
    }

    public String getThemeOfTopic() {
        return themeOfTopic;
    }

    public String getDateOfPublication() {
        return dateOfPublication;
    }

    public String getAuthorPseudo() {
        return authorPseudo;
    }

    public String getContent() {
        return content;
    }
}
