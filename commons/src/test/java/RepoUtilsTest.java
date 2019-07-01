import com.sdehunt.commons.util.RepoUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RepoUtilsTest {

    @Test
    public void trimRepoTest() {
        List<String> inputRepos = Arrays.asList(
                "https://github.com/facebook/react",
                "https://github.com/facebook/react.git",
                "facebook/react",
                "react"
        );

        String expected = "react";

        for (String inputRepo : inputRepos) {
            Assert.assertEquals(expected, RepoUtils.trimRepo(inputRepo));
        }

    }
}
