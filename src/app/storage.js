import store from "store2";

export function save(namespace, key, value) {
    const storage = namespace ? store.namespace(namespace) : store;
    try {
        storage.set(key, value);
    } catch (err) {
        // Do nothing on save local failure
    }
    storage.session.set(key, value);
}

export function get(namespace, key, alt) {
    const storage = namespace ? store.namespace(namespace) : store;
    return storage.session.get(key) || storage.get(key) || alt;
}

export function remove(namespace, key) {
    const storage = namespace ? store.namespace(namespace) : store;

    storage.session.remove(key);
    storage.remove(key);
}

export function clear(namespace) {
    const storage = namespace ? store.namespace(namespace) : store;
    storage.session.clear();
    storage.clear();
}

export default {
    save,
    get,
    remove,
    clear
}
