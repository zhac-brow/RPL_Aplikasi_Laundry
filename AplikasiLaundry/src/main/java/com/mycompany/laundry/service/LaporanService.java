package com.mycompany.laundry.service;

import java.time.LocalDate;
import java.util.List;
import com.mycompany.laundry.dao.TransaksiDAO;
import com.mycompany.laundry.model.Transaksi;
import org.springframework.transaction.annotation.Transactional;

public class LaporanService {

    private TransaksiDAO transaksiDAO;

    public void setTransaksiDAO(TransaksiDAO transaksiDAO) {
        this.transaksiDAO = transaksiDAO;
    }

    @Transactional(readOnly = true)
    public List<Transaksi> laporan(LocalDate dari, LocalDate sampai) {
        return transaksiDAO.antaraTanggal(dari, sampai);
    }
}
