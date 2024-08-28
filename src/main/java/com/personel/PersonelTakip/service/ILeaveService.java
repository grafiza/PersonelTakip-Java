package com.personel.PersonelTakip.service;

import com.personel.PersonelTakip.entity.Leave;

public interface ILeaveService extends IService<Leave> {

    Leave update(Long id, Leave leave);
}
