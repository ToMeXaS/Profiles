package lt.tomexas.profiles.utils;

public final class ProgressBar {

    public static String getProgressBar(double progress, double maxProgress, char barEmptyChar, char endEmptyChar, char barHalfChar, char endHalfChar, char barFullChar, char endFullChar, int length) {
        double percentage = progress / maxProgress * 100.0;
        StringBuilder progressBar = new StringBuilder();
        double percentPerChar = 1.0 / (double) length * 100.0;
        String unicodeChar = "\uF801"; // Include the Unicode character

        for (int i = 0; i < length; ++i) {
            double progressPassBar = percentPerChar * (double) (i + 1);

            if (percentage >= progressPassBar) {
                if (i + 1 == length) {
                    progressBar.append(unicodeChar).append(endFullChar);
                } else {
                    progressBar.append(unicodeChar).append(barFullChar);
                }
            } else if (percentage > progressPassBar - percentPerChar) {
                if (i + 1 == length) {
                    progressBar.append(unicodeChar).append(endHalfChar);
                } else {
                    progressBar.append(unicodeChar).append(barHalfChar);
                }
            } else {
                if (i + 1 == length) {
                    progressBar.append(unicodeChar).append(endEmptyChar);
                } else {
                    progressBar.append(unicodeChar).append(barEmptyChar);
                }
            }
        }

        return progressBar.toString();
    }
}
