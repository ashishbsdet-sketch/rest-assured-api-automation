package dev.ashish.qa.data;

import org.testng.annotations.DataProvider;

public final class PostData {
    private PostData() {
    }

    @DataProvider(name = "existingPostIds", parallel = true)
    public static Object[][] existingPostIds() {
        return new Object[][] {
                {1, 1},
                {25, 3},
                {100, 10}
        };
    }
}
