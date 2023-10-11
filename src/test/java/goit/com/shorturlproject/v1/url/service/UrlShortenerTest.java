package goit.com.shorturlproject.v1.url.service;

import goit.com.shorturlproject.v1.url.dto.UrlLink;
import goit.com.shorturlproject.v1.url.exceptions.UrlNotValidException;
import goit.com.shorturlproject.v1.user.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UrlShortenerTest {

    private UrlShortener urlShortener;

    @Mock
    private UrlValidator urlValidator;

    @Mock
    private UrlService urlService;

    @Mock
    private ShortUrlGenerator urlGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        urlShortener = new UrlShortener(urlValidator, urlService, urlGenerator);
    }

    @Test
    void testShortenValidURL() {
        String longURL = "https://www.example.com";
        String shortKey = "abc123";
        UrlLink urlLink = new UrlLink();
        urlLink.setLongUrl(longURL);
        urlLink.setShortUrl(shortKey);
        User user = new User(); // Create a User object for testing purposes

        when(urlValidator.isValidURL(longURL)).thenReturn(true);
        when(urlService.findUrlLinkByLongUrl(longURL)).thenReturn(Optional.empty());
        when(urlGenerator.getKey()).thenReturn(shortKey);
        when(urlService.saveAndFlush(shortKey, longURL, user)).thenReturn(urlLink);

        UrlLink result = urlShortener.shortenURL(longURL, user);

        assertEquals(urlLink, result);
        verify(urlValidator, times(1)).isValidURL(longURL);
        verify(urlService, times(1)).findUrlLinkByLongUrl(longURL);
        verify(urlGenerator, times(1)).getKey();
        verify(urlService, times(1)).saveAndFlush(shortKey, longURL, user);
    }

    @Test
    void testShortenExistingURL() {
        String longURL = "https://www.example.com";
        String shortKey = "abc123";
        UrlLink urlLink = new UrlLink();
        urlLink.setLongUrl(longURL);
        urlLink.setShortUrl(shortKey);
        User user = new User();
        urlLink.setUser(user);

        when(urlValidator.isValidURL(longURL)).thenReturn(true);
        when(urlService.findUrlLinkByLongUrl(longURL)).thenReturn(Optional.of(urlLink));

        UrlLink result = urlShortener.shortenURL(longURL, user);

        assertEquals(urlLink, result);
        verify(urlValidator, times(1)).isValidURL(longURL);
        verify(urlService, times(1)).findUrlLinkByLongUrl(longURL);
        verify(urlGenerator, never()).getKey();
        verify(urlService, never()).saveAndFlush(any(), any(), any());
    }

    @Test
    void testShortenInvalidURL() {
        String longURL = "invalid_url";
        User user = new User(); // Create a User object for testing purposes

        when(urlValidator.isValidURL(longURL)).thenReturn(false);

        assertThrows(UrlNotValidException.class, () -> urlShortener.shortenURL(longURL, user));
        verify(urlValidator, times(1)).isValidURL(longURL);
        verify(urlService, never()).findUrlLinkByLongUrl(any());
        verify(urlGenerator, never()).getKey();
        verify(urlService, never()).saveAndFlush(any(), any(), any());
    }
}
