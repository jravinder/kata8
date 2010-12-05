package kata8
import static groovyx.gpars.dataflow.DataFlow.task;

class WordController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def upload = {}
    def uploadWords =
    {
        def wordsSix =[]

        new File("C:\\words.txt").eachLine{
            line->
            def wordInstance = new Word()
            
            if (line.size() <=6){
                //       wordsSix << line
                wordInstance.dictionaryWord = line

                wordInstance.save(flush: true)

            }
            //wordInstance.dictionaryWord = line

            //wordInstance.save(flush: true)
        }
        new File("myFile.txt").write(wordsSix)
    }

    def reIndex=
    {
        Word.get(1).reindex()
        Word.get(2).reindex()    }

    def searchWords ={
        def words = Word.list() 
        def wordsFound = []
        def firstLetter = ""
        def firstTwoLetters = ""
        def  firstThreeLetters = ""
        def  secondThreeLetters = ""

        def  firstFourLetters = ""
        def  firstFiveLetters = ""
        def lastTwoLetters = ""
        def   lastFourLetters = ""
        def   lastFiveLetters = ""
        def  lastOneLetter = ""

        words.each{
            if (it.dictionaryWord.size() == 6)
            {
                firstLetter = it.dictionaryWord[0]
                firstTwoLetters = it.dictionaryWord[0..1]
               
                firstThreeLetters = it.dictionaryWord[0..2]
                firstFourLetters = it.dictionaryWord[0..3]
                firstFiveLetters = it.dictionaryWord[0..4]
                          
                lastTwoLetters = it.dictionaryWord[4..5]
                lastFourLetters = it.dictionaryWord[2..5]
                lastFiveLetters = it.dictionaryWord[1..5]
                lastOneLetter = it.dictionaryWord[5]
                print firstLetter+"\n"
                print firstTwoLetters+"\n"
                print firstThreeLetters+"\n"
                print firstFourLetters+"\n"
                print firstFiveLetters+"\n"
                print lastTwoLetters+"\n"
                print lastFourLetters+"\n"
                print lastFiveLetters+"\n"
                print lastOneLetter+"\n"



                if(searchWord(firstLetter) && searchWord(lastFiveLetters))
                {wordsFound << firstLetter+"+"+lastFiveLetters}
           
                if(searchWord(firstTwoLetters) && searchWord(lastFourLetters))
                {wordsFound << firstTwoLetters+"+"+lastFourLetters}

                if(searchWord(firstThreeLetters) && searchWord(secondThreeLetters))
                {wordsFound << firstThreeLetters+"+"+secondThreeLetters}
            
                if(searchWord(firstFourLetters) && searchWord(lastTwoLetters))
                {wordsFound << firstFourLetters+"+"+lastTwoLetters}
            
                if(searchWord(firstFiveLetters) && searchWord(lastOneLetter))
                {wordsFound << firstFiveLetters+"+"+lastOneLetter}

            
            }
        }
        [wordsFound:wordsFound, total:wordsFound.size]
    }

    def searchWord(word) {
        String wordToSearch = word
        def results = Word.search(wordToSearch)
        // print results.total
    
        if (results.total == 0)
        return false
        else
        return true

    }
    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [wordInstanceList: Word.list(params), wordInstanceTotal: Word.count()]
    }

    def create = {
        def wordInstance = new Word()
        wordInstance.properties = params
        return [wordInstance: wordInstance]
    }

    def save = {
        def wordInstance = new Word(params)
        if (wordInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'word.label', default: 'Word'), wordInstance.id])}"
            redirect(action: "show", id: wordInstance.id)
        }
        else {
            render(view: "create", model: [wordInstance: wordInstance])
        }
    }

    def show = {
        def wordInstance = Word.get(params.id)
        if (!wordInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'word.label', default: 'Word'), params.id])}"
            redirect(action: "list")
        }
        else {
            [wordInstance: wordInstance]
        }
    }

    def edit = {
        def wordInstance = Word.get(params.id)
        if (!wordInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'word.label', default: 'Word'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [wordInstance: wordInstance]
        }
    }

    def update = {
        def wordInstance = Word.get(params.id)
        if (wordInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (wordInstance.version > version) {
                    
                    wordInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'word.label', default: 'Word')] as Object[], "Another user has updated this Word while you were editing")
                    render(view: "edit", model: [wordInstance: wordInstance])
                    return
                }
            }
            wordInstance.properties = params
            if (!wordInstance.hasErrors() && wordInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'word.label', default: 'Word'), wordInstance.id])}"
                redirect(action: "show", id: wordInstance.id)
            }
            else {
                render(view: "edit", model: [wordInstance: wordInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'word.label', default: 'Word'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def wordInstance = Word.get(params.id)
        if (wordInstance) {
            try {
                wordInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'word.label', default: 'Word'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'word.label', default: 'Word'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'word.label', default: 'Word'), params.id])}"
            redirect(action: "list")
        }
    }
}
