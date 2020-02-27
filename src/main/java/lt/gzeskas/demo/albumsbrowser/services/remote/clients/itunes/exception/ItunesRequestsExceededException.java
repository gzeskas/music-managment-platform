package lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class ItunesRequestsExceededException extends RuntimeException {
}
