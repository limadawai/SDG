package com.jica.sdg.service;

import com.jica.sdg.model.User;
import com.jica.sdg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> findOne(String userName) {
        return (List<User>) userRepository.findByUserName(userName);
    }

	@Override
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public Optional<User> findOne(Integer id) {
		return (Optional<User>) userRepository.findById(id);
	}

	@Override
	public List findByProvince(String id_prov) {
		return userRepository.findByProvince(id_prov);
	}

	@Override
	public void saveUsere(User rol) {
		userRepository.save(rol);
	}

	@Override
	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}

	@Override
	public List findAllGrid() {
		return userRepository.findAllGrid();
	}

	@Override
	public Integer cekUsername(String username) {
		return userRepository.cekUsername(username);
	}
}
