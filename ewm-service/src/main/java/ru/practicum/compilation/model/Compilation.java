package ru.practicum.compilation.model;

import lombok.*;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilation")
public class Compilation {
    @Id
    @Column(name = "compilation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean pinned;

    private String title;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_compilation",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;

    public Compilation(Boolean pinned, String title) {
        this.pinned = pinned;
        this.title = title;
    }
}
