package edu.agh.wfiis.solid.srp.example2;
import java.io.File;

public class SingleResponsibilityPrincipleManager {

    public String provideDescription() {
        return "SRP description";
    }

    public String refactorCode(String badCode) {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "code refactored using SRP";
    }

}


class BadCodeExtractor {

    public static String extractBadCodeFromFile(File file) {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "bad code from file";
    }

    public static String extractBadCodeFromClassPath(ClassLoader classLoader) {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "bad code from class loader";
    }
}


class Coursework {

        public static String generateCourseworkForStudents() {
        /* some magic happens here, irrelevant from this example perspective...*/
        return "very difficult coursework";
    }
}