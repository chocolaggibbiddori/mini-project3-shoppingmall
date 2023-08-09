package com.withJ.service;

import com.withJ.dao.AddressDAO;
import com.withJ.dao.MemberDAO;
import com.withJ.dto.AddressVO;
import com.withJ.dto.MemberVO;

import java.util.List;

/**
 * 유저와 관련된 비즈니스 로직을 처리하는 서비스
 *
 * @author kangdonghee
 */
public class MemberService {

    private static final MemberService INSTANCE = new MemberService();

    private final MemberDAO memberDAO;
    private final AddressDAO addressDAO;

    private MemberService() {
        memberDAO = MemberDAO.getInstance();
        addressDAO = AddressDAO.getInstance();
    }

    public static MemberService getInstance() {
        return INSTANCE;
    }

    /**
     * 동 이름을 입력하면 해당하는 주소들의 정보를 반환하는 메서드
     *
     * @param dong 동 이름
     * @return 동 이름에 해당하는 주소 목록
     * @author kangdonghee
     */
    public List<AddressVO> getAddressListByDong(String dong) {
        return addressDAO.selectAddressByDong(dong);
    }

    /**
     * 아이디 중복 체크를 수행하는 메서드
     *
     * @param id 사용자 아이디
     * @return 사용자 아이디가 중복이면 1, 그렇지 않으면 -1을 반환
     * @author kangdonghee
     */
    public int duplicateCheckId(String id) {
        return memberDAO.confirmID(id);
    }

    /**
     * 회원 가입을 수행하는 메서드
     *
     * @param memberVO 회원 가입 정보
     * @author kangdonghee
     */
    public void joinMember(MemberVO memberVO) {
        memberDAO.insertMember(memberVO);
    }

    /**
     * 유저 아이디를 입력 받아 유저 객체를 반환하는 메서드
     *
     * @param id 사용자 아이디
     * @return 유저 정보가 담긴 객체
     * @author kangdonghee
     */
    public MemberVO getMemberById(String id) {
        return memberDAO.getMember(id);
    }

    /**
     * 아이디와 비밀번호를 검사하는 메서드
     * 아이디를 DB에서 확인해서 유저 객체로 받아온 후, 비밀번호를 비교한다.
     * 아이디가 DB에 저장되어 있지 않다면 유저 객체가 null이므로, 해당 객체를 null과 비교해서 아이디의 존재를 확인한다.
     * 아이디가 저장되어 있었다면 유저 객체에 비밀번호 값이 저장되어 있으므로, 그 값을 입력 받은 비밀번호와 비교한다.
     *
     * @param id 검사할 아이디
     * @param pwd 검사할 비밀번호
     * @return 아이디와 비밀번호가 DB에 저장된 값과 일치하면 true, 그렇지 않으면 false를 반환
     * @author kangdonghee
     */
    public boolean isValidMember(String id, String pwd) {
        MemberVO memberVO = getMemberById(id);
        return memberVO != null && memberVO.getPwd().equals(pwd);
    }
}
