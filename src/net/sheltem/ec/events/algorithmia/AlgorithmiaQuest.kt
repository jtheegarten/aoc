package net.sheltem.ec.events.algorithmia

import net.sheltem.ec.events.Quest

abstract class AlgorithmiaQuest<T>(test: List<T>) : Quest<T>("Algorithmia", test)
