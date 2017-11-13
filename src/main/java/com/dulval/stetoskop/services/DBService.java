package com.dulval.stetoskop.services;

import com.dulval.stetoskop.domain.Address;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dulval.stetoskop.domain.City;
import com.dulval.stetoskop.domain.Doctor;
import com.dulval.stetoskop.domain.InterationMedicament;
import com.dulval.stetoskop.domain.ItemPrescription;
import com.dulval.stetoskop.domain.Medicament;
import com.dulval.stetoskop.domain.Pacient;
import com.dulval.stetoskop.domain.Prescription;
import com.dulval.stetoskop.domain.State;
import com.dulval.stetoskop.domain.enums.Role;
import com.dulval.stetoskop.repositories.AddressRepository;
import com.dulval.stetoskop.repositories.CityRepository;
import com.dulval.stetoskop.repositories.DoctorRepository;
import com.dulval.stetoskop.repositories.InterationMedicamentRepository;
import com.dulval.stetoskop.repositories.ItemPrescriptionRepository;
import com.dulval.stetoskop.repositories.MedicamentRepository;
import com.dulval.stetoskop.repositories.PacientRepository;
import com.dulval.stetoskop.repositories.PrescriptionRepository;
import com.dulval.stetoskop.repositories.StateRepository;
import com.google.common.collect.Sets;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;

@Service
public class DBService {
    
    @Autowired
    private BCryptPasswordEncoder pe;
    
    @Autowired
    private StateRepository estadoRepository;
    
    @Autowired
    private PacientRepository pacRepository;
    
    @Autowired
    private CityRepository cidadeRepository;
    
    @Autowired
    private AddressRepository addRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private MedicamentRepository medicamentRepository;
    
    @Autowired
    private InterationMedicamentRepository interationMedicamentRepository;
    
    @Autowired
    private PrescriptionRepository presRepository;
    @Autowired
    private ItemPrescriptionRepository itemRepository;
    
    public void instantiateTestDatabase() throws ParseException {
        
        State est1 = new State(null, "Minas Gerais");
        State est2 = new State(null, "São Paulo");
        
        City c1 = new City(null, "Uberlândia", est1);
        City c2 = new City(null, "São Paulo", est2);
        City c3 = new City(null, "Campinas", est2);
        
        est1.getCitys().addAll(Arrays.asList(c1));
        est2.getCitys().addAll(Arrays.asList(c2, c3));
        
        estadoRepository.save(Arrays.asList(est1, est2));
        cidadeRepository.save(Arrays.asList(c1, c2, c3));
        
        Address add = new Address();
        add.setCep("37564000");
        add.setCity(c3);
        add.setNeighborhood("centro");
        add.setNumber("56");
        add.setStreet("Alameda");
        
        addRepository.save(add);
        
        Doctor doc = new Doctor();
        doc.setAccount(1);
        doc.setAddress(add);
        doc.setCrm("123456");
        doc.setEmail("diegodifreitas@gmail.com");
        doc.setName("Diego");
        doc.setPassword(pe.encode("123456"));
        doc.setPhone("3445-2264");
        doc.setProfession("Ginecologista");
        doc.addRoles(Role.ADMIN);
        
        doctorRepository.save(doc);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        
        Medicament a = new Medicament(null, "A");
        a.setApresentations(Sets.newHashSet("Remedio A", "Fabricado por Y"));
        a.setComercialNames(Sets.newHashSet("REMEDIO A", "REMEDIO AA"));
        Medicament b = new Medicament(null, "B");
        b.setApresentations(Sets.newHashSet("Remedio B", "Fabricado por W"));
        b.setComercialNames(Sets.newHashSet("REMEDIO B", "REMEDIO BB"));
        Medicament c = new Medicament(null, "C");
        c.setApresentations(Sets.newHashSet("Remedio C", "Fabricado por Q"));
        c.setComercialNames(Sets.newHashSet("REMEDIO C", "REMEDIO CC"));
        Medicament d = new Medicament(null, "D");
        
        InterationMedicament im1 = new InterationMedicament(a, b, "im A B");
        InterationMedicament im2 = new InterationMedicament(a, c, "im A C");
        InterationMedicament im3 = new InterationMedicament(b, c, "im B C");
        InterationMedicament im4 = new InterationMedicament(c, a, "im C A");
        InterationMedicament im5 = new InterationMedicament(c, d, "im C D");
        
        a.getInterations().add(im1);
        a.getInterations().add(im2);
        a.getMedicamentInteration().add(im4);
        b.getInterations().add(im3);
        b.getMedicamentInteration().add(im1);
        c.getMedicamentInteration().add(im2);
        c.getMedicamentInteration().add(im3);
        c.getInterations().add(im4);
        c.getInterations().add(im5);
        d.getMedicamentInteration().add(im5);
        
        medicamentRepository.save(Arrays.asList(a, b, c, d));
        interationMedicamentRepository.save(Arrays.asList(im1, im2, im3, im4, im5));
        
        Pacient pac = new Pacient();
        pac.setAddress(add);
        pac.setBirthdate(Date.from(Instant.now()));
        pac.setDoctor(doc);
        pac.setName("Gisele");
        pac.setEmail(("gigi@gmail.com"));
        pac.setPhone("3445-2264");
        pac.setProfession("Arquiteta");
        
        pacRepository.save(pac);
        
        Prescription pres = new Prescription();
        pres.setDate(Date.from(Instant.now()));
        pres.setPacient(pac);
        pres.setDescription("Se presitirem os sintomas um medico deve ser consultado.");
        
        ItemPrescription item = new ItemPrescription();
        item.setDescription("Tomar de 8 em 8 horas");
        item.setMedicament(a);
        item.setQuantity(10);
        
        item.setApresentation((String) a.getApresentations().iterator().next());
        item.setComercialName(a.getComercialNames());
        item.setUnities(Sets.newHashSet("Comprimidos"));
        item.setUseTypes(Sets.newHashSet("Oral"));
        item.setPrescription(pres);
        
        presRepository.save(pres);
        itemRepository.save(item);
        
    }
}
