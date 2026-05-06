package com.digis.PokedexApi.dto.pokemon;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PokeListResponseDTO {

    private int count;
    private String next;
    private String previous;
    private List<PokeListItemDTO> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PokeListItemDTO> getResults() {
        return results;
    }

    public void setResults(List<PokeListItemDTO> results) {
        this.results = results;
    }
    
}
