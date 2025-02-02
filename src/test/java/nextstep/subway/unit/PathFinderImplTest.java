package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.common.exception.CustomException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.PathType;
import nextstep.subway.domain.path.finder.PathFinder;
import nextstep.subway.domain.path.finder.PathFinderImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PathFinderImplTest {

  private Station 교대역;
  private Station 강남역;
  private Station 양재역;
  private Station 남부터미널역;
  private Line 신분당선;
  private Line 이호선;
  private Line 삼호선;

  @BeforeEach
  void setUp() {
    교대역 = new Station("교대역");
    강남역 = new Station("강남역");
    양재역 = new Station("양재역");
    남부터미널역 = new Station("남부터미널역");

    신분당선 = new Line("신분당선", "red");
    이호선 = new Line("2호선", "red");
    삼호선 = new Line("3호선", "red");

    신분당선.addSection(강남역, 양재역, 3, 3);
    이호선.addSection(교대역, 강남역, 6, 6);
    삼호선.addSection(교대역, 남부터미널역, 5, 5);
    삼호선.addSection(남부터미널역, 양재역, 5, 5);
  }

  @Test
  void 최단_거리_경로_조회() {
    // given
    List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
    PathFinder pathFinder = new PathFinderImpl(lines);

    // when
    Path path = pathFinder.findPath(교대역, 양재역, PathType.DISTANCE, 30);

    // then
    assertAll(
        () -> assertThat(path.getStations()).containsExactlyInAnyOrder(교대역, 강남역, 양재역),
        () -> assertThat(path.extractDistance()).isEqualTo(9),
        () -> assertThat(path.extractDuration()).isEqualTo(9),
        () -> assertThat(path.extractFare()).isEqualTo(1_250)
    );
  }

  @Test
  void 반대로_최단_거리_경로_조회() {
    // given
    List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
    PathFinder pathFinder = new PathFinderImpl(lines);

    // when
    Path path = pathFinder.findPath(양재역, 교대역, PathType.DISTANCE, 30);

    // then
    assertAll(
        () -> assertThat(path.getStations()).containsExactlyInAnyOrder(양재역, 강남역, 교대역),
        () -> assertThat(path.extractDistance()).isEqualTo(9),
        () -> assertThat(path.extractDuration()).isEqualTo(9),
        () -> assertThat(path.extractFare()).isEqualTo(1_250)
    );
  }

  @Test
  void 최소_시간_경로_조회() {
    // given
    List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
    PathFinder pathFinder = new PathFinderImpl(lines);

    // when
    Path path = pathFinder.findPath(교대역, 양재역, PathType.DURATION, 30);

    // then
    assertAll(
        () -> assertThat(path.getStations()).containsExactlyInAnyOrder(교대역, 강남역, 양재역),
        () -> assertThat(path.extractDistance()).isEqualTo(9),
        () -> assertThat(path.extractDuration()).isEqualTo(9),
        () -> assertThat(path.extractFare()).isEqualTo(1_250)
    );
  }

  @Test
  void 반대로_최소_시간_경로_조회() {
    // given
    List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
    PathFinder pathFinder = new PathFinderImpl(lines);

    // when
    Path path = pathFinder.findPath(양재역, 교대역, PathType.DURATION, 30);

    // then
    assertAll(
        () -> assertThat(path.getStations()).containsExactlyInAnyOrder(양재역, 강남역, 교대역),
        () -> assertThat(path.extractDistance()).isEqualTo(9),
        () -> assertThat(path.extractDuration()).isEqualTo(9),
        () -> assertThat(path.extractFare()).isEqualTo(1_250)
    );
  }

  @Test
  void 경로_조회_타입_에러() {
    // given
    List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
    PathFinder pathFinder = new PathFinderImpl(lines);

    // when
    assertThatThrownBy(() -> pathFinder.findPath(양재역, 교대역, null, 30)).isInstanceOf(CustomException.class);
  }
}
