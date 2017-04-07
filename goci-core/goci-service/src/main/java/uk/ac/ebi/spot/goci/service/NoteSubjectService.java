package uk.ac.ebi.spot.goci.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.spot.goci.model.Note;
import uk.ac.ebi.spot.goci.model.NoteSubject;
import uk.ac.ebi.spot.goci.repository.NoteSubjectRepository;

import java.util.Collection;

/**
 * Created by xinhe on 06/04/2017.
 */
@Service
public class NoteSubjectService {
    private NoteSubjectRepository noteSubjectRepository;

    public NoteSubjectService(NoteSubjectRepository noteSubjectRepository) {
        this.noteSubjectRepository = noteSubjectRepository;
    }

    public NoteSubject findOne(Long id){
        return noteSubjectRepository.findOne(id);
    }

    public Collection<NoteSubject> findAll(){
        return noteSubjectRepository.findAll();
    }


    protected NoteSubject findDefaultSubject() {
        NoteSubject subject = noteSubjectRepository.findAll().get(0);
        return subject;
    }

    public NoteSubject findBySubject(String subject) {
        NoteSubject subjectSelected= noteSubjectRepository.findBySubjectIgnoreCase(subject);
        if (subjectSelected==null) {
            //xintodo add log
            subjectSelected = findDefaultSubject();
        }
        return subjectSelected;
    }

}
