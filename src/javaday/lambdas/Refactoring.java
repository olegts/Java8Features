package javaday.lambdas;

import javaday.lambdas.domain.Album;
import javaday.lambdas.domain.Track;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toSet;
import static javaday.lambdas.domain.SampleData.aLoveSupreme;
import static javaday.lambdas.domain.SampleData.sampleShortAlbum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Refactoring {

        interface LongTrackFinder {
            Set<String> findLongTracks(List<Album> albums);
        }

        static class Step0 implements LongTrackFinder {
            public Set<String> findLongTracks(List<Album> albums) {
                Set<String> trackNames = new HashSet<>();
                for(Album album : albums) {
                    for (Track track : album.getTrackList()) {
                        if (track.getLength() > 60) {
                            String name = track.getName();
                            trackNames.add(name);
                        }
                    }
                }
                return trackNames;
            }
        }

        static class Step1 implements LongTrackFinder {
            public Set<String> findLongTracks(List<Album> albums) {
                Set<String> trackNames = new HashSet<>();
                albums.stream()
                        .forEach(album -> {
                            album.getTracks()
                                    .forEach(track -> {
                                        if (track.getLength() > 60) {
                                            String name = track.getName();
                                            trackNames.add(name);
                                        }
                                    });
                        });
                return trackNames;
            }
        }

        static class Step2 implements LongTrackFinder {
            public Set<String> findLongTracks(List<Album> albums) {
                Set<String> trackNames = new HashSet<>();
                albums.stream()
                        .forEach(album -> {
                            album.getTracks()
                                    .filter(track -> track.getLength() > 60)
                                    .map(track -> track.getName())
                                    .forEach(name -> trackNames.add(name));
                        });
                return trackNames;
            }
        }

        static class Step3 implements LongTrackFinder {
            public Set<String> findLongTracks(List<Album> albums) {
                Set<String> trackNames = new HashSet<>();

                albums.stream()
                        .flatMap(album -> album.getTracks())
                        .filter(track -> track.getLength() > 60)
                        .map(track -> track.getName())
                        .forEach(name -> trackNames.add(name));

                return trackNames;
            }
        }

        static class Step4 implements LongTrackFinder {
            public Set<String> findLongTracks(List<Album> albums) {
                return albums.stream()
                        .flatMap(album -> album.getTracks())
                        .filter(track -> track.getLength() > 60)
                        .map(track -> track.getName())
                        .collect(toSet());
            }
        }

    @Test
    public void allStringJoins() {
        List<Supplier<LongTrackFinder>> finders =
                Arrays.<Supplier<LongTrackFinder>>asList(
                Step0::new,
                Step1::new,
                Step2::new,
                Step3::new,
                Step4::new
        );

        List<Album> albums = unmodifiableList(asList(aLoveSupreme, sampleShortAlbum));
        List<Album> noTracks = unmodifiableList(asList(sampleShortAlbum));

        finders.forEach(finder -> {
            System.out.println("Testing: " + finder.toString());

            LongTrackFinder longTrackFinder = finder.get();
            Set<String> longTracks = longTrackFinder.findLongTracks(albums);

            assertEquals("[Acknowledgement, Resolution]", longTracks.toString());

            longTracks = longTrackFinder.findLongTracks(noTracks);

            assertTrue(longTracks.isEmpty());
        });
    }

}
