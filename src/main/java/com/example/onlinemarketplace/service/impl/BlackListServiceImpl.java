package com.example.onlinemarketplace.service.impl;

import com.example.onlinemarketplace.dto.request.BlackListRequest;
import com.example.onlinemarketplace.dto.response.BlackListDto;
import com.example.onlinemarketplace.exception.SellerNotFoundException;
import com.example.onlinemarketplace.exception.UserNotFoundException;
import com.example.onlinemarketplace.model.BlackList;
import com.example.onlinemarketplace.model.Seller;
import com.example.onlinemarketplace.model.User;
import com.example.onlinemarketplace.repository.BlackListRepository;
import com.example.onlinemarketplace.repository.SellerRepository;
import com.example.onlinemarketplace.repository.UserRepository;
import com.example.onlinemarketplace.service.BlackListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class BlackListServiceImpl implements BlackListService {

    private final BlackListRepository blackListRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<BlackListDto> findByUser(String username, Pageable paging) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Page<BlackList> page = blackListRepository.findByUser(user, paging);
        return new PageImpl<BlackListDto>(page.getContent().stream()
                .map(this::convertToBlackListDto)
                .collect(Collectors.toList()), paging, page.getTotalElements());

    }

    @Override
    public Integer countBlackListBySeller(String companyName) {
        Seller seller = sellerRepository.findByCompanyName(companyName)
                .orElseThrow(SellerNotFoundException::new);
        return blackListRepository.countBlackListBySeller(seller);
    }

    @Override
    public BlackList createNewBlackList(User user, Seller seller) {
        return blackListRepository.save(new BlackList(user, seller));
    }

    @Override
    public BlackList createNewBlackList(BlackList blackList) {
        return blackListRepository.save(blackList);
    }

    @Override
    public BlackListDto createNewBlackList(String username, String companyName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Seller seller = sellerRepository.findByCompanyName(companyName)
                .orElseThrow(SellerNotFoundException::new);

        return convertToBlackListDto(blackListRepository.save(new BlackList(user, seller)));
    }

    @Override
    public boolean delete(BlackListRequest blackList) {
        User user = userRepository.findByUsername(blackList.getUsername())
                .orElseThrow(UserNotFoundException::new);

        Seller seller = sellerRepository.findByCompanyName(blackList.getCompanyName())
                .orElseThrow(SellerNotFoundException::new);

        BlackList object = blackListRepository.findByUserAndSeller(user, seller);
        blackListRepository.delete(object);
        return !blackListRepository.existsById(object.getId());
    }

    @Override
    public boolean delete(User user, Seller seller) {
        BlackList blackList = blackListRepository.findByUserAndSeller(user, seller);
        Long id = blackList.getId();
        blackListRepository.delete(blackList);
        return !blackListRepository.existsById(id);
    }

    @Override
    public boolean delete(String username, String companyName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Seller seller = sellerRepository.findByCompanyName(companyName)
                .orElseThrow(SellerNotFoundException::new);
        BlackList blackList = blackListRepository.findByUserAndSeller(user, seller);
        Long id = blackList.getId();
        blackListRepository.delete(blackList);
        return !blackListRepository.existsById(id);
    }

    @Override
    public void deleteAllByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        blackListRepository.deleteAllByUser(user);
    }

    public BlackListDto convertToBlackListDto(BlackList blackList) {
        return modelMapper.map(blackList, BlackListDto.class);
    }
}
