package com.example.soap;

import com.example.soap.generated.CreateUserRequest;
import com.example.soap.generated.CreateUserResponse;
import com.example.soap.generated.GetUserRequest;
import com.example.soap.generated.GetUserResponse;
import com.example.soap.generated.User;
import com.example.soap.generated.UserNotFoundFault;
import com.example.soap.generated.UserNotFoundFault_Exception;
import com.example.soap.generated.UserPortType;
import jakarta.jws.WebService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * WSDL에서 생성된 UserPortType 인터페이스의 구현체.
 * 비즈니스 로직은 이 클래스에만 작성합니다. (현재는 메모리 저장소 사용)
 */
@WebService(
        serviceName = "UserService",
        portName = "UserPort",
        targetNamespace = "http://example.com/user",
        endpointInterface = "com.example.soap.generated.UserPortType",
        wsdlLocation = "classpath:wsdl/UserService.wsdl")
public class UserServiceImpl implements UserPortType {

    private final Map<Long, User> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong();

    public UserServiceImpl() {
        // 테스트용 초기 데이터
        save("홍길동", "hong@example.com");
    }

    @Override
    public GetUserResponse getUser(GetUserRequest request) throws UserNotFoundFault_Exception {
        User user = store.get(request.getId());
        if (user == null) {
            UserNotFoundFault fault = new UserNotFoundFault();
            fault.setId(request.getId());
            fault.setMessage("User not found: " + request.getId());
            throw new UserNotFoundFault_Exception(fault.getMessage(), fault);
        }
        GetUserResponse response = new GetUserResponse();
        response.setUser(user);
        return response;
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        CreateUserResponse response = new CreateUserResponse();
        response.setUser(save(request.getName(), request.getEmail()));
        return response;
    }

    private User save(String name, String email) {
        User user = new User();
        user.setId(seq.incrementAndGet());
        user.setName(name);
        user.setEmail(email);
        store.put(user.getId(), user);
        return user;
    }
}
