package com.arsltech.developer.MovisdoApp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Bound Service que interactua con el sync adapter para correr las sincronizaciones
 */
public class MovisdoSyncService extends Service {

    // Instancia del sync adapter
    private static MovisdoSyncAdapter movisdoSyncAdapter = null;

    // Objeto para prevenir errores entre hilos
    private static final Object lock = new Object();

    @Override
    public void onCreate() {
        synchronized (lock) {
            if (movisdoSyncAdapter == null) {
                movisdoSyncAdapter = new MovisdoSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    /**
     * Retorna interfaz de comunicación para que el sistema llame al sync adapter
     */
    @Override
    public IBinder onBind(Intent intent) {
        return movisdoSyncAdapter.getSyncAdapterBinder();
    }
}